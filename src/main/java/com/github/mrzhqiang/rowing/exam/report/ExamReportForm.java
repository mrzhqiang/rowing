package com.github.mrzhqiang.rowing.exam.report;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 考试报告表单。
 * <p>
 */
@Projection(name = "exam-report-form", types = {ExamReport.class})
public interface ExamReportForm extends BaseProjection {

    @Value("#{target.exam?.id}")
    String getExamId();

    Integer getPlanCount();

    Integer getActualCount();

    Integer getPassCount();

    BigDecimal getPassRate();

    BigDecimal getFullScore();

    BigDecimal getMaxScore();

    BigDecimal getMinScore();

    BigDecimal getAvgScore();

    BigDecimal getDifficultyRate();

}
