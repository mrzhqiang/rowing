package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 试题选项信息。
 * <p>
 */
@Projection(name = "exam-question-option-info", types = {ExamQuestionOption.class})
public interface ExamQuestionOptionInfo extends BaseProjection {

    String getLabel();

    String getContent();

    /**
     * TODO 考生信息不应该返回这个内容，否则将暴露正确答案
     */
    Boolean getRighted();

}
