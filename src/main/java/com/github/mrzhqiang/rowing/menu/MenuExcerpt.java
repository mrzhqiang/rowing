package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.Logic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 菜单摘录。
 * <p>
 * 表示菜单列表的一个预览数据。
 */
@Projection(types = {Menu.class}, name = "menu-excerpt")
public interface MenuExcerpt {

    Long getId();

    @Value("#{target.parent?.id}")
    Long getParentId();

    @SuppressWarnings("unused")
    String getTitle();

    @SuppressWarnings("unused")
    String getIcon();

    @SuppressWarnings("unused")
    String getFullPath();

    @SuppressWarnings("unused")
    LocalDateTime getCreated();

    @Value("#{target.lastModified}")
    LocalDateTime getUpdated();

    Integer getOrdered();

    @SuppressWarnings("unused")
    Logic getEnabled();

    @SuppressWarnings("unused")
    List<MenuExcerpt> getChildren();

}
