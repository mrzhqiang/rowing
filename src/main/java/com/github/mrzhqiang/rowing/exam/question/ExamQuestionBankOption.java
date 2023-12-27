package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 题库选项。
 * <p>
 */
@Projection(name = "exam-question-bank-option", types = {ExamQuestionBank.class})
public interface ExamQuestionBankOption extends BaseProjection {

    String getTitle();

}
