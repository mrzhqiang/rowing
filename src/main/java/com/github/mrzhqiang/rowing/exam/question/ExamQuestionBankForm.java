package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * 题库表单。
 * <p>
 */
@Projection(name = "exam-question-bank-form", types = {ExamQuestionBank.class})
public interface ExamQuestionBankForm extends BaseProjection {

    String getTitle();

    @Value("#{target.subject.value}")
    String getSubjectValue();

    String getDescription();

    List<ExamQuestionExcerpt> getQuestions();

}
