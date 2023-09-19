package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * 考试规则摘要。
 * <p>
 */
@Projection(name = "exam-rule-excerpt", types = {ExamRule.class})
public interface ExamRuleExcerpt extends AuditableProjection {

    Duration getDuration();

    Integer getTotalScore();

    BigDecimal getPassLine();

    Integer getPassScore();

}
