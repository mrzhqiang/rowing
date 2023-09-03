package com.github.mrzhqiang.rowing.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

/**
 * 审计摘要。
 * <p>
 * 将审计字段转为审计摘要字段时，提供统一的风格。
 */
@Projection(name = "auditable-excerpt", types = {AuditableEntity.class})
public interface AuditableExcerpt extends BaseExcerpt {

    LocalDateTime getCreated();

    String getCreatedBy();

    @Value("#{target.lastModified}")
    LocalDateTime getUpdated();

    @Value("#{target.lastModifiedBy}")
    String getUpdatedBy();

}
