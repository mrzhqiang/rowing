package com.github.mrzhqiang.rowing.exam.rule;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import com.github.mrzhqiang.rowing.exam.mode.ExamModeExcerpt;
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

    default BigDecimal getPassLinePercent() {
        return getPassLine().multiply(BigDecimal.valueOf(100));
    }

    Integer getPassScore();

    String getStrategy();

    List<ExamModeExcerpt> getModes();

}
