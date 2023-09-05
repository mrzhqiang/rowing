package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.BaseExcerpt;
import org.springframework.data.rest.core.config.Projection;

/**
 * 初始化任务表单。
 * <p>
 */
@Projection(name = "init-task-form", types = {InitTask.class})
public interface InitTaskForm extends BaseExcerpt {

    String getName();

    String getPath();

    String getType();

    String getStatus();

    Integer getOrdered();

    String getDiscard();

}
