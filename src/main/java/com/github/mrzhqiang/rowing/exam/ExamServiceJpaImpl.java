package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.helper.random.RandomNumbers;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.account.AccountRepository;
import com.github.mrzhqiang.rowing.account.RunAsSystem;
import com.github.mrzhqiang.rowing.domain.ExamModeStrategy;
import com.github.mrzhqiang.rowing.domain.ExamStatus;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.json.EnumTranslator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceJpaImpl implements ExamService {

    private final ExamRepository repository;
    private final AccountRepository accountRepository;
    private final ExamPaperRepository paperRepository;
    private final ExamPaperAnswerGroupRepository groupRepository;
    private final ExamQuestionRepository questionRepository;
    private final ExamPaperAnswerRepository answerRepository;
    private final EnumTranslator enumTranslator;

    public ExamServiceJpaImpl(ExamRepository repository,
                              AccountRepository accountRepository,
                              ExamPaperRepository paperRepository,
                              ExamPaperAnswerGroupRepository groupRepository,
                              ExamQuestionRepository questionRepository,
                              ExamPaperAnswerRepository answerRepository,
                              EnumTranslator enumTranslator) {
        this.repository = repository;
        this.accountRepository = accountRepository;
        this.paperRepository = paperRepository;
        this.groupRepository = groupRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.enumTranslator = enumTranslator;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTakers(Long examId, List<Long> takers) {
        Optional.ofNullable(examId)
                .flatMap(repository::findById)
                .ifPresent(it -> {
                    Exams.validateUpdate(it);
                    if (CollectionUtils.isEmpty(takers)) {
                        it.setTakers(Collections.emptyList());
                    } else {
                        List<Account> accounts = accountRepository.findAllById(takers);
                        it.setTakers(accounts);
                    }
                    // 注意：如果涉及事件处理相关的改动，需要主动发布 BeforeSaveEvent 事件
                    repository.save(it);
                    // 注意：如果涉及事件处理相关的改动，需要主动发布 AfterSaveEvent 事件
                });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMarkers(Long examId, List<Long> markers) {
        Optional.ofNullable(examId)
                .flatMap(repository::findById)
                .ifPresent(it -> {
                    Exams.validateUpdate(it);
                    if (CollectionUtils.isEmpty(markers)) {
                        it.setMarkers(Collections.emptyList());
                    } else {
                        List<Account> accounts = accountRepository.findAllById(markers);
                        it.setMarkers(accounts);
                    }
                    repository.save(it);
                });
    }

    @Override
    public Page<ExamInfo> myExamInfo(UserDetails userDetails,
                                     String title,
                                     String code,
                                     LocalDateTime firstStart,
                                     LocalDateTime secondStart,
                                     Pageable pageable) {
        return accountRepository.findByUsername(userDetails.getUsername())
                .map(it -> repository.findAllByTakersContainingAndTitleContainingAndCodeContainingAndStartTimeBetween(
                        it, title, code, firstStart, secondStart, pageable))
                .orElse(Page.empty());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void prepare(Long id) {
        repository.findById(id)
                .ifPresent(it -> {
                    Exams.validateUpdate(it);
                    this.generatePaper(it);
                    it.setStatus(ExamStatus.WAITING);
                    repository.save(it);
                });
    }

    private void generatePaper(Exam entity) {
        // TODO paper service
        Exams.validateWaiting(entity);
        List<Account> takers = entity.getTakers();
        List<Account> markers = entity.getMarkers();
        for (Account taker : takers) {
            ExamPaper paper = new ExamPaper();
            paper.setExam(entity);
            paper.setTaker(taker);
            // 如果存在多位阅卷人，那么随机分配；否则取第一位阅卷人
            if (markers.size() > 1) {
                paper.setMarker(markers.get(RandomNumbers.nextInt(markers.size())));
            } else {
                paper.setMarker(markers.get(0));
            }
            paperRepository.save(paper);
            // 为试卷生成大题
            generateAnswerGroup(entity, paper);
        }
    }

    private void generateAnswerGroup(Exam entity, ExamPaper paper) {
        // TODO group service
        ExamRule rule = entity.getRule();
        List<ExamMode> modes = rule.getModes();
        for (ExamMode mode : modes) {
            ExamPaperAnswerGroup group = new ExamPaperAnswerGroup();
            // paper 必须先进行 save 才能作为引用字段
            group.setPaper(paper);
            group.setOrdered(mode.getOrdered());
            String subtitle = enumTranslator.asText(mode.getType());
            // TODO 先硬编码，后面再设置模板
            group.setTitle(Strings.lenientFormat("%s.%s", mode.getOrdered(), subtitle));
            group.setTotalScore(mode.getScore());
            groupRepository.save(group);
            // 根据规则生成小题列表
            ExamModeStrategy strategy = rule.getStrategy();
            List<ExamQuestion> questions = generateQuestion(entity, mode, strategy);
            for (ExamQuestion question : questions) {
                ExamPaperAnswer answer = new ExamPaperAnswer();
                answer.setGroup(group);
                answer.setQuestion(question);
                answerRepository.save(answer);
            }
        }
    }

    private List<ExamQuestion> generateQuestion(Exam entity, ExamMode mode, ExamModeStrategy strategy) {
        List<ExamQuestion> questionList = Lists.newArrayList();
        if (ExamModeStrategy.FIXED.equals(strategy)) {
            questionList.add(mode.getQuestion1());
            questionList.add(mode.getQuestion2());
            questionList.add(mode.getQuestion3());
            questionList.add(mode.getQuestion4());
            questionList.add(mode.getQuestion5());
            return questionList;
        }

        ExamQuestionBank bank = new ExamQuestionBank();
        bank.setSubject(entity.getSubject());
        ExamQuestion question = new ExamQuestion();
        question.setBank(bank);
        question.setType(mode.getType());
        // 随机抽题
        if (ExamModeStrategy.RANDOM.equals(strategy)) {
            Collections.shuffle(questionList);
        }
        // 匹配最佳
        if (ExamModeStrategy.MATCH.equals(strategy)) {
            question.setDifficulty(entity.getDifficulty());
        }
        questionList = questionRepository.findAll(Example.of(question));
        if (questionList.size() > mode.getAmount()) {
            questionList = questionList.subList(0, mode.getAmount());
        }
        return questionList;
    }

    @Transactional(rollbackFor = Exception.class)
    @RunAsSystem
    @Override
    public void updateStatus() {
        updateRunningFromWaiting();
        updateMarkingFromRunning();
        updateFinishedFromMarking();
    }

    private void updateRunningFromWaiting() {
        Exam probe = new Exam();
        probe.setStatus(ExamStatus.WAITING);
        // TODO 如果存在大量考试，则应该优化这里的逻辑，使用 Example 的匹配策略来查询已开始且未结束的考试
        repository.findAll(Example.of(probe)).stream()
                .filter(it -> LocalDateTime.now().isAfter(it.getStartTime())
                        && LocalDateTime.now().isBefore(it.getEndTime()))
                .peek(it -> it.setStatus(ExamStatus.RUNNING))
                .forEach(repository::save);
    }

    private void updateMarkingFromRunning() {
        Exam probe = new Exam();
        probe.setStatus(ExamStatus.RUNNING);
        // TODO 如果存在大量考试，则应该优化这里的逻辑，使用 Example 的匹配策略来查询已结束的考试
        repository.findAll(Example.of(probe)).stream()
                .filter(it -> LocalDateTime.now().isAfter(it.getEndTime()))
                .peek(it -> it.setStatus(ExamStatus.MARKING))
                .forEach(repository::save);
    }

    private void updateFinishedFromMarking() {
        Exam probe = new Exam();
        probe.setStatus(ExamStatus.MARKING);
        // TODO 如果存在大量考试，则应该优化这里的逻辑，使用 Example 的匹配策略来查询已阅卷完毕的考试
        repository.findAll(Example.of(probe)).stream()
                .filter(it -> it.getPapers().stream().allMatch(ExamPaper::getFinished))
                .peek(it -> it.setStatus(ExamStatus.FINISHED))
                .forEach(repository::save);
    }

}
