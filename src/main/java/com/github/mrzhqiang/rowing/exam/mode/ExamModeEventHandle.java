package com.github.mrzhqiang.rowing.exam.mode;

import com.github.mrzhqiang.rowing.exam.rule.ExamRule;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 考试模式事件处理器。
 * <p>
 */
@RepositoryEventHandler
@Component
@RequiredArgsConstructor
public class ExamModeEventHandle {

    @HandleBeforeCreate
    public void onBeforeCreate(ExamMode entity) {
        BigDecimal score = entity.getScore();
        ExamRule rule = entity.getRule();
        List<ExamMode> modes = rule.getModes();
        BigDecimal sumScore = modes.stream()
                .map(ExamMode::getScore)
                .reduce(score, BigDecimal::add);
        BigDecimal totalScore = BigDecimal.valueOf(rule.getTotalScore());
        Preconditions.checkArgument(sumScore.subtract(totalScore).signum() <= 0,
                "分值和不能超过规则的总分数");
    }

    @HandleBeforeSave
    public void onBeforeSave(ExamMode entity) {
        BigDecimal score = entity.getScore();
        ExamRule rule = entity.getRule();
        List<ExamMode> modes = rule.getModes();
        BigDecimal sumScore = modes.stream()
                .filter(it -> !it.getId().equals(entity.getId()))
                .map(ExamMode::getScore)
                .reduce(score, BigDecimal::add);
        BigDecimal totalScore = BigDecimal.valueOf(rule.getTotalScore());
        Preconditions.checkArgument(sumScore.subtract(totalScore).signum() <= 0,
                "分值和不能超过规则的总分数");
    }

}
