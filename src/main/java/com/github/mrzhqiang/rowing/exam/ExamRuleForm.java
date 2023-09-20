package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.util.List;

/**
 * 考试规则表单。
 * <p>
 */
@Projection(name = "exam-rule-form", types = {ExamRule.class})
public interface ExamRuleForm extends BaseProjection {

    String getTitle();

    Long getDuration();

    Integer getTotalScore();

    BigDecimal getPassLine();

    Integer getPassScore();

    String getStrategy();

    List<ExamModeExcerpt> getModes();

}
