package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.util.List;

/**
 * 试卷大题表单。
 */
@Projection(name = "exam-question-group-form", types = ExamPaperAnswerGroup.class)
public interface ExamPaperAnswerGroupForm extends BaseProjection {

    @Value("#{target.paper?.id}")
    String getPaperId();

    Integer getOrdered();

    String getTitle();

    BigDecimal getTotalScore();

    BigDecimal getSumScore();

    List<ExamPaperAnswer> getAnswers();

}
