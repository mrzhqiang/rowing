package com.github.mrzhqiang.rowing.exam.report;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

/**
 * 考试报告摘要。
 * <p>
 */
@Projection(name = "exam-report-excerpt", types = {ExamReport.class})
public interface ExamReportExcerpt extends AuditableProjection {

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
