package com.github.mrzhqiang.rowing.exam.paper;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 试卷答题卡摘要。
 */
@Projection(name = "exam-question-card-excerpt", types = ExamPaperAnswerCard.class)
public interface ExamPaperAnswerCardExcerpt extends AuditableProjection {

    Integer getOrdered();

    String getTitle();

    BigDecimal getTotalScore();

    BigDecimal getSumScore();

}
