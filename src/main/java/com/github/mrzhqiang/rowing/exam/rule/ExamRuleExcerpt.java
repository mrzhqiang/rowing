package com.github.mrzhqiang.rowing.exam.rule;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import com.github.mrzhqiang.rowing.domain.ExamModeStrategy;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 考试规则摘要。
 * <p>
 */
@Projection(name = "exam-rule-excerpt", types = {ExamRule.class})
public interface ExamRuleExcerpt extends AuditableProjection {

    String getTitle();

    Long getDuration();

    Integer getTotalScore();

    BigDecimal getPassLine();

    Integer getPassScore();

    ExamModeStrategy getStrategy();

}
