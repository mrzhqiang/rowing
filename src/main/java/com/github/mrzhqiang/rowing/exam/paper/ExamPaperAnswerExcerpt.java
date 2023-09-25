package com.github.mrzhqiang.rowing.exam.paper;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 试卷答题摘要。
 * <p>
 */
@Projection(name = "exam-paper-answer-excerpt", types = {ExamPaperAnswer.class})
public interface ExamPaperAnswerExcerpt extends AuditableProjection {

    String getAnswer();

    String getAnswerUrl();

    String getComment();

    BigDecimal getScore();

}
