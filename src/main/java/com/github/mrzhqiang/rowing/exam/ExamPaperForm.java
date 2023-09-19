package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷表单。
 * <p>
 */
@Projection(name = "exam-paper-form", types = {ExamPaper.class})
public interface ExamPaperForm extends AuditableProjection {

    LocalDateTime getAnswerStart();

    Boolean getSubmitted();

    LocalDateTime getSubmitTime();

    LocalDateTime getMarkStart();

    Boolean getFinished();

    LocalDateTime getFinishTime();

    String getResult();

    BigDecimal getTotalScore();

    List<ExamPaperAnswerGroupForm> getGroups();

}
