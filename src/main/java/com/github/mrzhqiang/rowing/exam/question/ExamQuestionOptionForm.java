package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 试题选项表单。
 * <p>
 */
@Projection(name = "exam-question-option-form", types = {ExamQuestionOption.class})
public interface ExamQuestionOptionForm extends BaseProjection {

    @Value("#{target.question?.id}")
    String getQuestionId();

    String getLabel();

    String getContent();

    Boolean getRighted();

    BigDecimal getScoreRatio();

}
