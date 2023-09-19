package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import com.github.mrzhqiang.rowing.domain.ExamModeStrategy;
import com.github.mrzhqiang.rowing.domain.ExamQuestionType;
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

    ExamModeStrategy getStrategy();

}
