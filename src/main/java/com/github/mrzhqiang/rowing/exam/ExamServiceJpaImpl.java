package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.helper.random.RandomNumbers;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.account.AccountRepository;
import com.github.mrzhqiang.rowing.account.RunAsSystem;
import com.github.mrzhqiang.rowing.domain.ExamModeStrategy;
import com.github.mrzhqiang.rowing.domain.ExamQuestionType;
import com.github.mrzhqiang.rowing.domain.ExamStatus;
import com.github.mrzhqiang.rowing.exam.mode.ExamMode;
import com.github.mrzhqiang.rowing.exam.paper.ExamPaper;
import com.github.mrzhqiang.rowing.exam.paper.ExamPaperAnswer;
import com.github.mrzhqiang.rowing.exam.paper.ExamPaperAnswerCard;
import com.github.mrzhqiang.rowing.exam.paper.ExamPaperAnswerCardRepository;
import com.github.mrzhqiang.rowing.exam.paper.ExamPaperAnswerRepository;
import com.github.mrzhqiang.rowing.exam.paper.ExamPaperInfo;
import com.github.mrzhqiang.rowing.exam.paper.ExamPaperRepository;
import com.github.mrzhqiang.rowing.exam.paper.ExamPapers;
import com.github.mrzhqiang.rowing.exam.question.ExamQuestion;
import com.github.mrzhqiang.rowing.exam.question.ExamQuestionBank;
import com.github.mrzhqiang.rowing.exam.question.ExamQuestionOption;
import com.github.mrzhqiang.rowing.exam.question.ExamQuestionOptionRepository;
import com.github.mrzhqiang.rowing.exam.question.ExamQuestionRepository;
import com.github.mrzhqiang.rowing.exam.rule.ExamRule;
import com.github.mrzhqiang.rowing.exception.ResourceNotFoundException;
import com.google.common.base.Preconditions;
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
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamServiceJpaImpl implements ExamService {

    private final ExamRepository repository;
    private final AccountRepository accountRepository;
    private final ExamPaperRepository paperRepository;
    private final ExamPaperAnswerCardRepository cardRepository;
    private final ExamQuestionRepository questionRepository;
    private final ExamPaperAnswerRepository answerRepository;
    private final ExamQuestionOptionRepository optionRepository;
    private final EnumTranslator enumTranslator;

    public ExamServiceJpaImpl(ExamRepository repository,
                              AccountRepository accountRepository,
                              ExamPaperRepository paperRepository,
                              ExamPaperAnswerCardRepository cardRepository,
                              ExamQuestionRepository questionRepository,
                              ExamPaperAnswerRepository answerRepository,
                              ExamQuestionOptionRepository optionRepository,
                              EnumTranslator enumTranslator) {
        this.repository = repository;
        this.accountRepository = accountRepository;
        this.paperRepository = paperRepository;
        this.cardRepository = cardRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.optionRepository = optionRepository;
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
    public Page<ExamInfo> takerExamInfo(UserDetails userDetails,
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

    @Override
    public Page<ExamInfo> markerExamInfo(UserDetails userDetails,
                                         String title,
                                         String code,
                                         LocalDateTime firstStart,
                                         LocalDateTime secondStart,
                                         Pageable pageable) {
        return accountRepository.findByUsername(userDetails.getUsername())
                .map(it -> repository.findAllByMarkersContainingAndTitleContainingAndCodeContainingAndStartTimeBetween(
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

    @Transactional(rollbackFor = Exception.class)
    @RunAsSystem
    @Override
    public void updateStatus() {
        updateRunningFromWaiting();
        updateMarkingFromRunning();
        updateFinishedFromMarking();
    }

    @Override
    public ExamPaperInfo findTakerPaper(String username, Long examId) {
        Exam exam = repository.findById(examId)
                //.filter(Exams::validateTake)
                .orElseThrow(() -> ResourceNotFoundException.of("操作失败，考试未找到！"));
        Preconditions.checkState(accountRepository.existsAccountByUsernameAndTakeExamsContaining(username, exam),
                "操作失败，用户无权参与考试！");
        return paperRepository.findByTaker_UsernameAndExam(username, exam)
                .orElseThrow(() -> ResourceNotFoundException.of("操作失败，试卷未找到！"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void takerPaper(Long paperId) {
        paperRepository.findById(paperId)
                .filter(ExamPapers::checkTake)
                .filter(it -> Objects.isNull(it.getAnswerStart()))
                .ifPresent(it -> {
                    it.setAnswerStart(LocalDateTime.now());
                    paperRepository.save(it);
                });
    }

    @Override
    public ExamPaperInfo findMarkerPaper(String username, Long examId) {
        Exam exam = repository.findById(examId)
                //.filter(Exams::validateMark)
                .orElseThrow(() -> ResourceNotFoundException.of("操作失败，考试未找到！"));
        Preconditions.checkState(accountRepository.existsAccountByUsernameAndMarkExamsContaining(username, exam),
                "操作失败，用户无权批阅试卷！");
        return paperRepository.findByMarker_UsernameAndExam(username, exam)
                //.filter(ExamPapers::checkMark)
                .orElseThrow(() -> ResourceNotFoundException.of("操作失败，试卷未找到！"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void markerPaper(Long paperId) {
        paperRepository.findById(paperId)
                .filter(ExamPapers::checkMark)
                .filter(it -> Objects.isNull(it.getMarkStart()))
                .ifPresent(it -> {
                    it.setMarkStart(LocalDateTime.now());
                    paperRepository.save(it);
                });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void savePaperAnswer(PaperAnswerData data) {
        // TODO 检测用户是否支持修改数据
        answerRepository.findById(data.getId()).ifPresent(it -> patchPaperAnswer(data, it));
    }

    @Override
    public void savePaperMarker(PaperAnswerData data) {
        // TODO 检测用户是否支持修改数据
        answerRepository.findById(data.getId()).ifPresent(it -> patchPaperMarker(data, it));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void commitPaper(Long paperId, PaperAnswerData data) {
        answerRepository.findById(data.getId()).ifPresent(it -> patchPaperAnswer(data, it));
        paperRepository.findById(paperId).ifPresent(this::autoMarkPaper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void finishPaper(Long paperId, PaperAnswerData data) {
        answerRepository.findById(data.getId()).ifPresent(it -> patchPaperAnswer(data, it));
        paperRepository.findById(paperId).ifPresent(it -> {
            it.setFinished(true);
            it.setFinishTime(LocalDateTime.now());
            BigDecimal totalScore = BigDecimal.ZERO;
            List<ExamPaperAnswerCard> cards = it.getCards();
            // TODO 优化这里的代码，使用方法封装嵌套循环，减少过多的 if 语句
            if (!CollectionUtils.isEmpty(cards)) {
                for (ExamPaperAnswerCard card : cards) {
                    BigDecimal sumScore = BigDecimal.ZERO;
                    List<ExamPaperAnswer> answers = card.getAnswers();
                    if (CollectionUtils.isEmpty(answers)) {
                        continue;
                    }
                    for (ExamPaperAnswer answer : answers) {
                        sumScore = sumScore.add(answer.getScore());
                    }
                    card.setSumScore(sumScore);
                    totalScore = totalScore.add(sumScore);
                    cardRepository.save(card);
                }
            }
            it.setTotalScore(totalScore);
            it.setResult(ExamPapers.generateResult(it));
            paperRepository.save(it);
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
            // 生成答题卡
            generatePaperAnswerCard(entity, paper);
        }
    }

    private void generatePaperAnswerCard(Exam entity, ExamPaper paper) {
        // TODO group service
        ExamRule rule = entity.getRule();
        List<ExamMode> modes = rule.getModes();
        for (ExamMode mode : modes) {
            ExamPaperAnswerCard card = new ExamPaperAnswerCard();
            // paper 必须先进行 save 才能作为引用字段
            card.setPaper(paper);
            card.setOrdered(mode.getOrdered());
            String subtitle = enumTranslator.asText(mode.getType());
            // TODO 先硬编码，后面再设置模板
            card.setTitle(Strings.lenientFormat("%s.%s", mode.getOrdered(), subtitle));
            card.setTotalScore(mode.getScore());
            cardRepository.save(card);
            // 生成答题列表
            generatePaperAnswer(entity, mode, card);
        }
    }

    private void generatePaperAnswer(Exam entity, ExamMode mode, ExamPaperAnswerCard card) {
        List<ExamQuestion> questionList = Lists.newArrayList();
        ExamModeStrategy strategy = entity.getRule().getStrategy();
        if (ExamModeStrategy.FIXED.equals(strategy)) {
            questionList.add(mode.getQuestion1());
            questionList.add(mode.getQuestion2());
            questionList.add(mode.getQuestion3());
            questionList.add(mode.getQuestion4());
            questionList.add(mode.getQuestion5());
        }

        ExamQuestionBank bank = new ExamQuestionBank();
        bank.setSubject(entity.getSubject());
        ExamQuestion probe = new ExamQuestion();
        probe.setBank(bank);
        probe.setType(mode.getType());
        // 匹配最佳
        if (ExamModeStrategy.MATCH.equals(strategy)) {
            probe.setDifficulty(entity.getDifficulty());
        }
        questionList = questionRepository.findAll(Example.of(probe));
        // 随机抽题
        if (ExamModeStrategy.RANDOM.equals(strategy)) {
            Collections.shuffle(questionList);
        }
        if (questionList.size() > mode.getAmount()) {
            questionList = questionList.subList(0, mode.getAmount());
        }

        for (int i = 0; i < questionList.size(); i++) {
            ExamQuestion question = questionList.get(i);
            ExamPaperAnswer answer = new ExamPaperAnswer();
            answer.setCard(card);
            answer.setQuestion(question);
            answer.setOrdered(i + 1);
            // 将答题卡分数平均分给试题列表
            BigDecimal questionScore = card.getTotalScore()
                    .divide(BigDecimal.valueOf(questionList.size()), RoundingMode.HALF_UP);
            answer.setQuestionScore(questionScore);
            answerRepository.save(answer);
        }
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
                .peek(it -> it.getPapers().forEach(this::autoMarkPaper))
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

    private void patchPaperAnswer(PaperAnswerData data, ExamPaperAnswer answer) {
        Long selectOptionId = data.getSelectOptionId();
        if (selectOptionId != null) {
            optionRepository.findById(selectOptionId).ifPresent(answer::setSelectOption);
        }
        List<Long> chooseOptionIds = data.getChooseOptionIds();
        if (!CollectionUtils.isEmpty(chooseOptionIds)) {
            answer.setChooseOptions(optionRepository.findAllById(chooseOptionIds));
        }
        if (StringUtils.hasText(data.getAnswer())) {
            answer.setAnswer(data.getAnswer());
        }
        if (StringUtils.hasText(data.getAnswerUrl())) {
            answer.setAnswerUrl(data.getAnswerUrl());
        }
        answerRepository.save(answer);
    }

    private void patchPaperMarker(PaperAnswerData data, ExamPaperAnswer answer) {
        if (!data.getScore().equals(answer.getScore())) {
            answer.setScore(data.getScore());
        }
        if (StringUtils.hasText(data.getComment())) {
            answer.setComment(data.getComment());
        }
        answerRepository.save(answer);
    }

    private void autoMarkPaper(ExamPaper paper) {
        paper.setSubmitted(true);
        paper.setSubmitTime(LocalDateTime.now());
        BigDecimal totalScore = BigDecimal.ZERO;
        List<ExamPaperAnswerCard> cards = paper.getCards();
        // TODO 优化这里的代码，使用方法封装嵌套循环，减少过多的 if 语句
        if (!CollectionUtils.isEmpty(cards)) {
            // 对客观题进行自动判卷
            for (ExamPaperAnswerCard card : cards) {
                BigDecimal sumScore = BigDecimal.ZERO;
                List<ExamPaperAnswer> answers = card.getAnswers();
                if (CollectionUtils.isEmpty(answers)) {
                    continue;
                }
                for (ExamPaperAnswer answer : answers) {
                    ExamQuestion question = answer.getQuestion();
                    ExamQuestionType questionType = question.getType();
                    // 客观题
                    if (ExamQuestionType.isObjective(questionType)) {
                        BigDecimal questionScore = answer.getQuestionScore();
                        if (ExamQuestionType.isSingleOption(questionType)) {
                            // 单个选项
                            ExamQuestionOption selectOption = answer.getSelectOption();
                            if (Boolean.TRUE.equals(selectOption.getRighted())) {
                                answer.setScore(questionScore);
                            }
                        } else {
                            // 多选题
                            List<ExamQuestionOption> chooseOptions = answer.getChooseOptions();
                            if (CollectionUtils.isEmpty(chooseOptions)) {
                                continue;
                            }
                            // 如果有一个错误答案，则不得分
                            if (chooseOptions.stream().anyMatch(option -> !option.getRighted())) {
                                continue;
                            }
                            List<ExamQuestionOption> options = question.getOptions();
                            if (CollectionUtils.isEmpty(options)) {
                                continue;
                            }
                            List<ExamQuestionOption> rightOptions = options.stream()
                                    .filter(ExamQuestionOption::getRighted)
                                    .collect(Collectors.toList());
                            // 检测是否选择部分正确答案，未完全选对
                            if (rightOptions.size() != chooseOptions.size()) {
                                // 如果是宽松模式，则按比例得分，否则不计分
                                if (Boolean.TRUE.equals(question.getSloppyMode())) {
                                    BigDecimal score = chooseOptions.stream()
                                            .map(ExamQuestionOption::getScoreRatio)
                                            .map(o -> o.multiply(questionScore))
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    answer.setScore(score);
                                }
                            } else {
                                answer.setScore(questionScore);
                            }
                        }
                    }
                    sumScore = sumScore.add(answer.getScore());
                    answerRepository.save(answer);
                }
                card.setSumScore(sumScore);
                totalScore = totalScore.add(sumScore);
                cardRepository.save(card);
            }
        }
        paper.setTotalScore(totalScore);
        paperRepository.save(paper);
    }

}
