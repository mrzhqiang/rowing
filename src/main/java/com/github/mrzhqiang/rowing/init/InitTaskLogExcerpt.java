package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 初始化任务日志摘要。
 * <p>
 */
@Projection(name = "init-task-log-excerpt", types = {InitTaskLog.class})
public interface InitTaskLogExcerpt extends AuditableProjection {

    String getMessage();

    String getTrace();

}
