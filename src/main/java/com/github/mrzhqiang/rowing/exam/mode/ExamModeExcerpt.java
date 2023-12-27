package com.github.mrzhqiang.rowing.exam.mode;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import com.github.mrzhqiang.rowing.domain.ExamQuestionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 考试模式摘要。
 * <p>
 */
@Projection(name = "exam-mode-excerpt", types = {ExamMode.class})
public interface ExamModeExcerpt extends AuditableProjection {

    Integer getOrdered();

    ExamQuestionType getType();

    BigDecimal getScore();

    Integer getAmount();

    @Value("#{target.question1?.code}")
    String getQuestion1Code();

    @Value("#{target.question2?.code}")
    String getQuestion2Code();

    @Value("#{target.question3?.code}")
    String getQuestion3Code();

    @Value("#{target.question4?.code}")
    String getQuestion4Code();

    @Value("#{target.question5?.code}")
    String getQuestion5Code();

}
