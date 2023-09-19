package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 考试模式表单。
 * <p>
 */
@Projection(name = "exam-mode-excerpt", types = {ExamMode.class})
public interface ExamModeForm extends BaseProjection {

    Integer getOrdered();

    String getType();

    BigDecimal getScore();

    Integer getAmount();

    String getStrategy();

}
