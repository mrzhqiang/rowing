package com.github.mrzhqiang.rowing.exam.mode;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 考试模式表单。
 * <p>
 */
@Projection(name = "exam-mode-form", types = {ExamMode.class})
public interface ExamModeForm extends BaseProjection {

    @Value("#{target.rule?.id}")
    String getRuleId();

    Integer getOrdered();

    String getType();

    BigDecimal getScore();

    Integer getAmount();

    @Value("#{target.question1?.id}")
    String getQuestion1Id();

    @Value("#{target.question2?.id}")
    String getQuestion2Id();

    @Value("#{target.question3?.id}")
    String getQuestion3Id();

    @Value("#{target.question4?.id}")
    String getQuestion4Id();

    @Value("#{target.question5?.id}")
    String getQuestion5Id();

}
