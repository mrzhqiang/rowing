package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 试题分组摘要。
 */
@Projection(name = "exam-question-group-excerpt", types = ExamPaperAnswerGroup.class)
public interface ExamPaperAnswerGroupExcerpt extends AuditableProjection {

    Integer getOrdered();

    String getTitle();

    BigDecimal getTotalScore();

    BigDecimal getSumScore();

}
