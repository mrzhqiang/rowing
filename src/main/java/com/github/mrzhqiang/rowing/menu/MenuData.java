package com.github.mrzhqiang.rowing.menu;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单数据。
 * <p>
 * 对应 classpath:data/menu.json 文件内容，用于初始化生成菜单数据。
 * <p>
 * 由于 menu.json 文件是从前端 src/router/index.js 转换而来，因此也支持返回给前端作为动态路由数据。
 * <p>
 * 注意：菜单数据不包含 404 兜底路由，因此前端必须在加载完菜单数据后，追加 404 兜底路由。
 * <p>
 * 代码如下：
 * <p>
 * { "path": "*", "redirect": "/404", "hidden": true }
 * </p>
 */
@Data
public class MenuData implements Serializable {

    private static final long serialVersionUID = 4045471755425912647L;

    /**
     * 菜单 ID。
     */
    private Long id;
    /**
     * 上级菜单 ID。
     * <p>
     * 如果为 null 表示当前菜单为顶级菜单。
     */
    private Long parentId;
    /**
     * 名称。
     * <p>
     * 用于前端的 {@code <keep-alive>} 属性。
     */
    private String name;
    /**
     * 路径。
     * <p>
     * 即浏览器中的 URL 路径，通常顶级菜单包含 / 前缀，子级菜单不包含。
     */
    private String path;
    /**
     * 完整路径。
     * <p>
     * 根据上级菜单的完整路径拼接当前菜单的路径，进行补全的完整路径。
     */
    private String fullPath;
    /**
     * 前端组件。
     * <p>
     * 导入页面时需要，通常是 <code>@/layout/**</code> 以及 <code>@/views/**</code> 组件地址。
     */
    private String component;
    /**
     * 重定向。
     * <p>
     * 由上级菜单指定，通常是第一个子级菜单的完整路径，表示访问上级菜单路径时，重定向到这个子级菜单。
     * <p>
     * 因为上级菜单通常不作为页面显示，仅作为"目录"存在，所以需要一个默认页面。
     * <p>
     * 当设置为 noRedirect 常量时，将无法从面包屑中跳转到当前菜单。
     */
    private String redirect;
    /**
     * 是否隐藏。
     * <p>
     * 如果设置为 true 将不在侧边栏展示。
     */
    private Boolean hidden = false;
    /**
     * 是否始终显示。
     * <p>
     * 如果设置为 ture，将始终显示在主菜单上，无论是否存在子菜单。
     * <p>
     * 如果未设置此项，则超过一个子菜单时，以嵌套方式展示，否则隐藏（如果存在一个子菜单，则显示这个子菜单）。
     */
    private Boolean alwaysShow;
    /**
     * 元数据。
     */
    private MenuMetaData meta;
    /**
     * 排序。
     */
    private Integer ordered;
    /**
     * 子级菜单列表。
     */
    private List<MenuData> children = Lists.newArrayList();
    /**
     * 菜单资源列表。
     */
    private List<MenuResourceData> resourceList = Lists.newArrayList();

}
