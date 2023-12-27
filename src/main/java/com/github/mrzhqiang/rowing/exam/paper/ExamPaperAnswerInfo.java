package com.github.mrzhqiang.rowing.exam.paper;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import com.github.mrzhqiang.rowing.exam.question.ExamQuestionInfo;
import com.github.mrzhqiang.rowing.exam.question.ExamQuestionOptionInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 试卷答题信息。
 * <p>
 */
@Projection(name = "exam-paper-answer-info", types = {ExamPaperAnswer.class})
public interface ExamPaperAnswerInfo extends BaseProjection {

    Integer getOrdered();

    ExamQuestionInfo getQuestion();

    ExamQuestionOptionInfo getSelectOption();

    @Value("#{target.selectOption?.id}")
    String getSelectOptionId();

    List<ExamQuestionOptionInfo> getChooseOptions();

    default List<String> getChooseOptionIds() {
        return getChooseOptions().stream()
                .map(BaseProjection::getId)
                .collect(Collectors.toList());
    }

    String getAnswer();

    String getAnswerUrl();

    String getComment();

    BigDecimal getQuestionScore();

    BigDecimal getScore();

    default Boolean getHasAnswer() {
        return Objects.nonNull(getSelectOptionId())
                || !ObjectUtils.isEmpty(getChooseOptions())
                || StringUtils.hasText(getAnswer())
                || StringUtils.hasText(getAnswerUrl());
    }

}
