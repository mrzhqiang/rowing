package com.github.mrzhqiang.rowing.menu;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单元数据。
 */
@Data
public class MenuMetaData implements Serializable {

    private static final long serialVersionUID = -3043726683161620090L;

    /**
     * 标题。
     * <p>
     * 一般是英文名称，在侧边栏以及面包屑中显示，可以通过前端 i18n 转换为中文。
     */
    private String title;
    /**
     * 图标。
     * <p>
     * 展示在侧边栏的图标。
     */
    private String icon;
    /**
     * 是否不缓存。
     * <p>
     * 如果设置为 true，页面将不会被缓存。
     */
    private Boolean noCache = false;
    /**
     * 是否固定。
     * <p>
     * 如果设置为 true 则在 TAG 列表中固定。
     */
    private Boolean affix;
    /**
     * 是否显示面包屑。
     * <p>
     * 如果设置为 false 则在面包屑中隐藏。
     */
    private Boolean breadcrumb = true;
    /**
     * 活动菜单。
     * <p>
     * 如果设置了路径，则在侧边栏高亮这个路径。
     * <p>
     * 通常是隐藏的子菜单需要设置此项为上级菜单路径，表示访问子菜单时，在侧边栏高亮上级菜单。
     */
    private String activeMenu;
    /**
     * 角色列表。
     * <p>
     * 表示菜单被哪些角色拥有，一般是角色的唯一标识符。
     */
    private List<String> roles = Lists.newArrayList();

}
