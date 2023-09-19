package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.dict.DictItem;
import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

/**
 * 考试表单。
 * <p>
 */
@Projection(name = "exam-form", types = {Exam.class})
public interface ExamForm extends BaseProjection {

    String getTitle();

    String getCode();

    DictItem getSubject();

    Integer getDifficulty();

    @Value("#{target.rule.id}")
    String getRuleId();

    String getStatus();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    String getDescription();

    Long getTakerCount();

    Long getMarkerCount();

    ExamReport getReport();

}
