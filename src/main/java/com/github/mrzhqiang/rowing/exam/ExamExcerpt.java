package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import com.github.mrzhqiang.rowing.domain.ExamStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

/**
 * 考试摘要。
 * <p>
 */
@Projection(name = "exam-excerpt", types = {Exam.class})
public interface ExamExcerpt extends AuditableProjection {

    String getTitle();

    String getCode();

    @Value("#{target.subject.label}")
    String getSubjectLabel();

    Integer getDifficulty();

    ExamStatus getStatus();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

}
