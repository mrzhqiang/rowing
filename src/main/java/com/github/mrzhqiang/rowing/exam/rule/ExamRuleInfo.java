package com.github.mrzhqiang.rowing.exam.rule;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 考试规则信息。
 * <p>
 */
@Projection(name = "exam-rule-info", types = {ExamRule.class})
public interface ExamRuleInfo extends BaseProjection {

    String getTitle();

    Long getDuration();

    Integer getTotalScore();

    BigDecimal getPassLine();

    Integer getPassScore();

}
