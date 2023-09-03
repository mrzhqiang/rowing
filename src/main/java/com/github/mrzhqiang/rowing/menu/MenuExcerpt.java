package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.AuditableExcerpt;
import com.github.mrzhqiang.rowing.domain.Logic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;


/**
 * 菜单摘要。
 * <p>
 */
@Projection(name = "menu-excerpt", types = {Menu.class})
public interface MenuExcerpt extends AuditableExcerpt {

    @Value("#{target.parent?.id}")
    String getParentId();

    String getIcon();

    String getTitle();

    String getPath();

    String getFullPath();

    String getComponent();

    Logic getEnabled();

}
