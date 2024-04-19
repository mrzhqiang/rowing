package com.github.mrzhqiang.rowing.exam.rule;

import com.github.mrzhqiang.rowing.exam.mode.ExamMode;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 考试规则事件处理器。
 * <p>
 */
@RepositoryEventHandler
@Component
@RequiredArgsConstructor
public class ExamRuleEventHandle {

    @HandleBeforeCreate
    @HandleBeforeSave
    public void onBeforeCreateAndSave(ExamRule entity) {
        List<ExamMode> modes = entity.getModes();
        if (!CollectionUtils.isEmpty(modes)) {
            BigDecimal sumScore = modes.stream()
                    .map(ExamMode::getScore)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalScore = BigDecimal.valueOf(entity.getTotalScore());
            Preconditions.checkArgument(sumScore.subtract(totalScore).signum() <= 0,
                    "规则的总分数不能低于模式的分值和");
        }
    }

}
