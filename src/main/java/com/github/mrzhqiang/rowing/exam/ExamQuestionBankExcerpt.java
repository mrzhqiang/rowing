package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 题库摘要。
 * <p>
 */
@Projection(name = "exam-question-bank-excerpt", types = {ExamQuestionBank.class})
public interface ExamQuestionBankExcerpt extends AuditableProjection {

    String getTitle();

    @Value("#{target.subject.label}")
    String getSubjectLabel();

    String getDescription();

}
