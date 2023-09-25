package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import com.github.mrzhqiang.rowing.domain.ExamQuestionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 试题摘要。
 * <p>
 */
@Projection(name = "exam-question-excerpt", types = {ExamQuestion.class})
public interface ExamQuestionExcerpt extends AuditableProjection {

    @Value("#{target.bank?.id}")
    String getBankId();

    @Value("#{target.bank?.title}")
    String getBankTitle();

    String getCode();

    ExamQuestionType getType();

    Integer getDifficulty();

    Boolean getSloppyMode();

    String getStem();

    String getRemark();

}
