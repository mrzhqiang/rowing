package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * 试题信息。
 * <p>
 */
@Projection(name = "exam-question-info", types = {ExamQuestion.class})
public interface ExamQuestionInfo extends BaseProjection {

    String getCode();

    String getType();

    Integer getDifficulty();

    String getStem();

    String getSolution();

    String getSolutionUrl();

    String getExplained();

    String getExplainedUrl();

    List<ExamQuestionOptionInfo> getOptions();

}
