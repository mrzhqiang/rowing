package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * 考试规则表单。
 * <p>
 */
@Projection(name = "exam-rule-form", types = {ExamRule.class})
public interface ExamRuleForm extends BaseProjection {

    Duration getDuration();

    Integer getTotalScore();

    BigDecimal getPassLine();

    Integer getPassScore();

}
