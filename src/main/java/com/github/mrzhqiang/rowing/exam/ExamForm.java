package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.account.AccountTransfer;
import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试表单。
 * <p>
 */
@Projection(name = "exam-form", types = {Exam.class})
public interface ExamForm extends BaseProjection {

    String getTitle();

    String getCode();

    @Value("#{target.subject.value}")
    String getSubjectValue();

    Integer getDifficulty();

    @Value("#{target.rule.id}")
    String getRuleId();

    String getStatus();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    String getDescription();

    List<AccountTransfer> getTakers();

    List<AccountTransfer> getMarkers();

}
