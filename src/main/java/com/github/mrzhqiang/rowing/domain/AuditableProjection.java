package com.github.mrzhqiang.rowing.domain;

import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

/**
 * 审计投影。
 * <p>
 * 转换审计实体时，提供统一的风格。
 */
@Projection(name = "auditable-projection", types = {AuditableEntity.class})
public interface AuditableProjection extends BaseProjection {

    String getCreatedBy();

    LocalDateTime getCreated();

    String getUpdatedBy();

    LocalDateTime getUpdated();

}
