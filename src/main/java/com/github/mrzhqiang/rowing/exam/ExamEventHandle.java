package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.exam.rule.ExamRule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 考试事件处理器。
 * <p>
 */
@RepositoryEventHandler
@Component
@RequiredArgsConstructor
public class ExamEventHandle {

    @HandleBeforeCreate
    public void onBeforeCreate(Exam entity) {
        if (!StringUtils.hasText(entity.getCode())) {
            entity.setCode(Exams.generateCode(entity));
        }

        ExamRule rule = entity.getRule();
        LocalDateTime startTime = entity.getStartTime();
        entity.setEndTime(startTime.plus(rule.getDuration(), ChronoUnit.SECONDS));
    }

    @HandleBeforeSave
    public void onBeforeSave(Exam entity) {
        Exams.validateUpdate(entity);

        ExamRule rule = entity.getRule();
        LocalDateTime startTime = entity.getStartTime();
        entity.setEndTime(startTime.plus(rule.getDuration(), ChronoUnit.SECONDS));
    }

}
