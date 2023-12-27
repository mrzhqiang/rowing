package com.github.mrzhqiang.rowing.exam.paper;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.util.List;

/**
 * 试卷答题卡信息。
 */
@Projection(name = "exam-paper-answer-card-info", types = ExamPaperAnswerCard.class)
public interface ExamPaperAnswerCardInfo extends BaseProjection {

    Integer getOrdered();

    String getTitle();

    BigDecimal getTotalScore();

    List<ExamPaperAnswerInfo> getAnswers();

}
