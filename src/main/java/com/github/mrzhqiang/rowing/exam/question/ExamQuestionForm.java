package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * 试题表单。
 * <p>
 */
@Projection(name = "exam-question-form", types = {ExamQuestion.class})
public interface ExamQuestionForm extends BaseProjection {

    @Value("#{target.bank?.id}")
    String getBankId();

    String getCode();

    String getType();

    Integer getDifficulty();

    Boolean getSloppyMode();

    String getStem();

    @Value("#{target.rightOption?.id}")
    String getRightOptionId();

    String getSolution();

    String getSolutionUrl();

    String getExplained();

    String getExplainedUrl();

    String getRemark();

    List<ExamQuestionOptionExcerpt> getOptions();

}
