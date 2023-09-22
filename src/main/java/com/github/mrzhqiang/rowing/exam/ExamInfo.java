package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import com.github.mrzhqiang.rowing.domain.ExamStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

/**
 * 考试信息。
 * <p>
 */
@Projection(name = "exam-info", types = {Exam.class})
public interface ExamInfo extends BaseProjection {

    String getTitle();

    @Value("#{target.subject?.label}")
    String getSubjectLabel();

    String getCode();

    Integer getDifficulty();

    ExamRuleInfo getRule();

    ExamStatus getStatus();

    @Value("#{target.status.name()}")
    String getStatusCode();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    String getDescription();

}
