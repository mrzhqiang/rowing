package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
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
public interface InitTaskExcerpt extends AuditableProjection {

    String getName();

    String getPath();

    TaskType getType();

    TaskStatus getStatus();

    Integer getOrdered();

    Logic getDiscard();

    /**
     * 枚举值通常会被国际化返回对应语言标签的内容，如果需要返回枚举代码，
     * 则设定返回类型为 {@link String}，随后 Spring Boot 的转换器会自动调用枚举的 {@link Enum#name()} 方法进行转换。
     */
    @Value("#{target.discard}")
    String getDiscardCode();

}
