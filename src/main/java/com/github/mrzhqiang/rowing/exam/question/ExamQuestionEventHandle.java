package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.ExamQuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 试题事件处理器。
 * <p>
 */
@RepositoryEventHandler
@Component
@RequiredArgsConstructor
public class ExamQuestionEventHandle {

    private final ExamQuestionOptionRepository optionRepository;

    @HandleBeforeCreate
    public void onBeforeCreate(ExamQuestion entity) {
        if (!StringUtils.hasText(entity.getCode())) {
            entity.setCode(ExamQuestions.generateCode(entity));
        }
    }

    @HandleAfterSave
    public void onAfterSave(ExamQuestion entity) {
        if (ExamQuestionType.isSingleOption(entity.getType())) {
            ExamQuestionOption rightOption = entity.getRightOption();
            if (rightOption != null) {
                if (!rightOption.getRighted()) {
                    rightOption.setRighted(true);
                    optionRepository.save(rightOption);
                }
                List<ExamQuestionOption> options = entity.getOptions();
                if (!CollectionUtils.isEmpty(options)) {
                    options.stream().filter(it -> !it.getId().equals(rightOption.getId()))
                            .peek(it -> it.setRighted(false))
                            .forEach(optionRepository::save);
                }
            }
        }
    }

}
