package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 菜单资源表单。
 */
@Projection(name = "menu-resource-form", types = {MenuResource.class})
public interface MenuResourceForm extends BaseProjection {

    @Value("#{target.menu.id}")
    String getMenuId();

    String getName();

    String getAuthority();

    Integer getOrdered();

}
