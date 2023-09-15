package com.github.mrzhqiang.rowing.menu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.role.Role;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 菜单。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Menu extends AuditableEntity {

    private static final long serialVersionUID = 2321642972583403963L;

    /**
     * 父级菜单。
     * <p>
     * 用于嵌套路由。
     */
    @ManyToOne
    private Menu parent;
    /**
     * 子级菜单。
     * <p>
     * 用于嵌套路由，生成路由的树级结构。
     */
    @ToString.Exclude
    @OrderBy("ordered ASC, created DESC")
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Menu> children = Lists.newArrayList();

    /**
     * 图标。
     * <p>
     * 展示在标题之前的图标，属于内置数据，但不作为枚举实现。
     * <p>
     * 可以在前端硬编码一个图标库列表，支持预览选择，得到相应的图标字符串，传参到后端进行保存。
     * <p>
     * 只需要匹配对应的图标字符串，即显示对应的图标。
     */
    @Size(max = Domains.MENU_ICON_LENGTH)
    @Column(length = Domains.MENU_ICON_LENGTH)
    private String icon;
    /**
     * 标题。
     * <p>
     * 标题一般展示在侧边栏以及面包屑中，由于是用户自己创建的内容，通常不支持国际化，也就意味着输入什么内容，就展示什么内容。
     * <p>
     * 未来或许可以借助第三方接口，来实现对应的内容国际化功能，然后再开发自己的内容国际化功能。
     */
    @NotBlank
    @Size(max = Domains.MENU_TITLE_LENGTH)
    @Column(length = Domains.MENU_TITLE_LENGTH)
    private String title;
    /**
     * 路径。
     * <p>
     * 路由路径，拼接上级路径后，就变成在浏览器中看到的路径。
     */
    @NotBlank
    @Size(max = Domains.MENU_PATH_LENGTH)
    @Column(nullable = false, length = Domains.MENU_PATH_LENGTH)
    private String path;
    /**
     * 名称。
     * <p>
     * 路由名称，用于 {@code <keep-alive>} 属性。
     * <p>
     * 一般情况下和路径保持一致，可能以首字母大写的单词形式存在，比如 path 路径变成 Path 名称。
     */
    @Size(max = Domains.MENU_PATH_LENGTH)
    @Column(length = Domains.MENU_PATH_LENGTH)
    private String name;
    /**
     * 完整路径。
     * <p>
     * 通常路径是一个相对路径，除顶级菜单外，其他路径都不包含路径标识符，即 '/' 符号。
     * <p>
     * 另外，如果不保留完整路径，则每次获取菜单数据时，都可能需要递归获取上级菜单路径进行拼接，效率比较差。
     * <p>
     * 但如果保留完整路径，则在修改上级菜单路径时，也要遍历子级菜单同步路径，否则将导致子级菜单失效。
     */
    @Size(max = Domains.HTTP_URL_PATH_LENGTH)
    @Column(nullable = false, length = Domains.HTTP_URL_PATH_LENGTH)
    private String fullPath;
    /**
     * 组件。
     * <p>
     * 前端的组件名称，或者前端的组件路径。
     * <p>
     * 注意：这个字段非常关键，如果设置错误，将无法正确展示页面。
     */
    @NotBlank
    @Size(max = Domains.MENU_COMPONENT_LENGTH)
    @Column(nullable = false, length = Domains.MENU_COMPONENT_LENGTH)
    private String component;
    /**
     * 重定向。
     * <p>
     * 通常可以不设置，将使用第一个子级菜单的路径作为重定向数据，但这就需要获取子级菜单的数据，也就意味着进行了一次数据库查询。
     * <p>
     * 为了避免这种情况，可以在这里冗余重定向字段，只有不存在时，才去获取子级菜单的路径。
     * <p>
     * 注意：与路径不同的是，这里必须是一个完整路径，即从顶级菜单开始，一级级拼接到指定菜单的路径。
     */
    @Size(max = Domains.HTTP_URL_PATH_LENGTH)
    @Column(length = Domains.HTTP_URL_PATH_LENGTH)
    private String redirect;
    /**
     * 活动菜单。
     * <p>
     * 如果设置了路径，则在侧边栏高亮匹配到的路径。
     */
    @Size(max = Domains.HTTP_URL_PATH_LENGTH)
    @Column(length = Domains.HTTP_URL_PATH_LENGTH)
    private String activeMenu;
    /**
     * 是否隐藏。
     * <p>
     * 隐藏的菜单不在侧边栏展示，默认为 false 表示不隐藏。
     */
    private Boolean hidden = false;
    /**
     * 是否始终显示。
     * <p>
     * 如果设置为 true 则菜单将始终显示在主菜单上。
     * <p>
     * 如果未设置此项，或设置为 false 时，当超过一个子菜单，以嵌套的方式展示，只有一个子菜单，仅显示子菜单（隐藏当前菜单）。
     */
    private Boolean alwaysShow;
    /**
     * 不缓存。
     * <p>
     * 如果设置为 true 则页面将不会被缓存，默认为 false。
     */
    private Boolean noCache = false;
    /**
     * 固定。
     * <p>
     * 如果设置为 true 则在标签栏中固定显示，不能移除。
     */
    private Boolean affix;
    /**
     * 面包屑。
     * <p>
     * 如果设置为 false 则在面包屑中隐藏，默认为 true。
     */
    private Boolean breadcrumb = true;
    /**
     * 排序。
     * <p>
     * 自定义排序的菜单，将具备最高优先级，其次按创建时间排序。
     */
    private Integer ordered;
    /**
     * 是否启用。
     * <p>
     * 未启用的菜单，将不返回数据给前端，默认为启用。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_NAME_LENGTH)
    private Logic enabled = Logic.YES;
    /**
     * 是否内置。
     * <p>
     * 内置的菜单不允许更新和删除，避免系统出现故障。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = Domains.ENUM_NAME_LENGTH)
    private Logic internal = Logic.NO;

    /**
     * 菜单角色列表。
     * <p>
     * 主要可以返回给前端，当前菜单支持哪些角色。
     */
    @JsonIgnore
    @ToString.Exclude
    @RestResource(path = "roles", rel = "roles")
    @ManyToMany(mappedBy = "menus")
    private List<Role> roles = Lists.newArrayList();
    /**
     * 菜单资源列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @RestResource(path = "resources", rel = "resources")
    @OneToMany(mappedBy = "menu", orphanRemoval = true)
    private List<MenuResource> resources = Lists.newArrayList();

}
