package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.util.List;

/**
 * 试卷作答表单。
 * <p>
 */
@Projection(name = "exam-paper-answer-form", types = {ExamPaperAnswer.class})
public interface ExamPaperAnswerForm extends BaseProjection {

    @Value("#{target.group?.id}")
    String getGroupId();

    @Value("#{target.question?.id}")
    String getQuestionId();

    @Value("#{target.selectOption?.id}")
    String getSelectOptionId();

    List<ExamQuestionOptionForm> getChooseOptions();

    String getAnswer();

    String getAnswerUrl();

    String getComment();

    BigDecimal getScore();

}
