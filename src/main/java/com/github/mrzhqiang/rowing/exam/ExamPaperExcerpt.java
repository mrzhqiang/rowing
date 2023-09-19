package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 试卷摘要。
 * <p>
 */
@Projection(name = "exam-paper-excerpt", types = {ExamPaper.class})
public interface ExamPaperExcerpt extends AuditableProjection {

    @Value("#{target.taker.username}")
    String getTakerUsername();

    @Value("#{target.marker.username}")
    String getMarkerUsername();

    LocalDateTime getAnswerStart();

    Boolean getSubmitted();

    LocalDateTime getSubmitTime();

    LocalDateTime getMarkStart();

    Boolean getFinished();

    LocalDateTime getFinishTime();

    BigDecimal getTotalScore();

}
