package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.ExamQuestionType;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 试题选项事件处理器。
 * <p>
 */
@RepositoryEventHandler
@Component
public class ExamQuestionOptionEventHandle {

    private final ExamQuestionOptionRepository repository;
    private final ExamQuestionRepository questionRepository;

    public ExamQuestionOptionEventHandle(ExamQuestionOptionRepository repository,
                                         ExamQuestionRepository questionRepository) {
        this.repository = repository;
        this.questionRepository = questionRepository;
    }

    @HandleAfterCreate
    @HandleAfterSave
    public void onBeforeCreate(ExamQuestionOption entity) {
        if (entity.getRighted()) {
            ExamQuestion question = entity.getQuestion();
            question.setRightOption(entity);
            questionRepository.save(question);
            List<ExamQuestionOption> options = question.getOptions();
            if (!CollectionUtils.isEmpty(options)
                    && !ExamQuestionType.MULTIPLE.equals(question.getType())) {
                // 存在选项列表，但不是多选题，则将其他选项重设为非正确答案
                options.stream()
                        .filter(it -> it.getRighted() && !it.getId().equals(entity.getId()))
                        .peek(it -> it.setRighted(false))
                        .forEach(repository::save);
            }
        }
    }

}
