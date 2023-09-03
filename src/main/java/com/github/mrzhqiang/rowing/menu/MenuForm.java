package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.BaseExcerpt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 菜单详情。
 * <p>
 * 用于查看、编辑的菜单详情，屏蔽无关字段，避免信息泄露。
 */
@Projection(name = "menu-detail", types = {Menu.class})
public interface MenuForm extends BaseExcerpt {

    @Value("#{target.parent?.id}")
    String getParentId();

    String getIcon();

    String getTitle();

    String getPath();

    String getComponent();

    String getRedirect();

    Integer getOrdered();

    String getEnabled();

    Boolean getHidden();

    String getActiveMenu();

    Boolean getAlwaysShow();

    Boolean getNoCache();

    Boolean getAffix();

    Boolean getBreadcrumb();

}
