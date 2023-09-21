package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.helper.random.RandomNumbers;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.ExamModeStrategy;
import com.github.mrzhqiang.rowing.domain.ExamStatus;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Example;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.webmvc.json.EnumTranslator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

/**
 * 考试事件处理器。
 * <p>
 */
@RepositoryEventHandler
@Component
public class ExamEventHandle {

    private final ExamPaperRepository paperRepository;
    private final ExamPaperAnswerGroupRepository groupRepository;
    private final ExamQuestionRepository questionRepository;
    private final ExamPaperAnswerRepository answerRepository;
    private final EnumTranslator enumTranslator;

    public ExamEventHandle(ExamPaperRepository paperRepository,
                           ExamPaperAnswerGroupRepository groupRepository,
                           ExamQuestionRepository questionRepository,
                           ExamPaperAnswerRepository answerRepository,
                           EnumTranslator enumTranslator) {
        this.paperRepository = paperRepository;
        this.groupRepository = groupRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.enumTranslator = enumTranslator;
    }

    @HandleBeforeCreate
    public void onBeforeCreate(Exam entity) {
        if (!StringUtils.hasText(entity.getCode())) {
            entity.setCode(Exams.generateCode(entity));
        }

        ExamRule rule = entity.getRule();
        LocalDateTime startTime = entity.getStartTime();
        entity.setEndTime(startTime.plus(rule.getDuration(), ChronoUnit.SECONDS));
    }

    @Transactional(rollbackFor = Exception.class)
    @HandleBeforeSave
    public void onBeforeSave(Exam entity) {
        ExamRule rule = entity.getRule();
        LocalDateTime startTime = entity.getStartTime();
        entity.setEndTime(startTime.plus(rule.getDuration(), ChronoUnit.SECONDS));

        // 如果是从 DEFAULT 到 WAITING 状态，那么根据考生名单创建试卷
        if (ExamStatus.WAITING.equals(entity.getStatus())) {
            generatePaper(entity);
        }
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

}
