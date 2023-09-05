package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.AuditableExcerpt;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.domain.TaskType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 初始化任务摘要。
 * <p>
 */
@Projection(name = "init-task-excerpt", types = {InitTask.class})
public interface InitTaskExcerpt extends AuditableExcerpt {

    String getName();

    String getPath();

    TaskType getType();

    TaskStatus getStatus();

    Integer getOrdered();

    Logic getDiscard();

    @Value("#{target.discard}")
    String getDiscardCode();

}
