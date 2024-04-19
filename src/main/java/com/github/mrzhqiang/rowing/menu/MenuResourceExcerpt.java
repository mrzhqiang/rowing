package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 菜单资源摘要。
 */
@Projection(name = "menu-resource-excerpt", types = {MenuResource.class})
public interface MenuResourceExcerpt extends AuditableProjection {

    String getName();

    Integer getOrdered();

}
