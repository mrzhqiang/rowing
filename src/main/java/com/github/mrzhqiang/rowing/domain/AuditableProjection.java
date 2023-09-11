package com.github.mrzhqiang.rowing.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

/**
 * 审计投影。
 * <p>
 * 转换审计实体时，提供统一的风格。
 */
@Projection(name = "auditable-projection", types = {AuditableEntity.class})
public interface AuditableProjection extends BaseProjection {

    LocalDateTime getCreated();

    String getCreatedBy();

    @Value("#{target.lastModified}")
    LocalDateTime getUpdated();

    @Value("#{target.lastModifiedBy}")
    String getUpdatedBy();

}
