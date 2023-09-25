package com.github.mrzhqiang.rowing.exam.rule;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 考试规则选项。
 * <p>
 */
@Projection(name = "exam-rule-option", types = {ExamRule.class})
public interface ExamRuleOption extends BaseProjection {

    String getTitle();

}
