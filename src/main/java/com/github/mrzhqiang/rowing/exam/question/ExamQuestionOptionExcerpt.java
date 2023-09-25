package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 试题选项摘要。
 * <p>
 */
@Projection(name = "exam-question-option-excerpt", types = {ExamQuestionOption.class})
public interface ExamQuestionOptionExcerpt extends AuditableProjection {

    String getLabel();

    String getContent();

    Boolean getRighted();

    BigDecimal getScoreRatio();

}
