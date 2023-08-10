package com.github.mrzhqiang.rowing.menu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.role.Role;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    /**
     * 非重定向值，表示在前端中，点击当前菜单的面包屑时，不具备跳转功能。
     */
    public static final String NO_REDIRECT = "noRedirect";

    /**
     * 父级菜单。
     * <p>
     * 用于嵌套路由。
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private Menu parent;
    /**
     * 子级菜单。
     * <p>
     * 用于嵌套路由，生成路由的树级结构。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Menu> children = Lists.newArrayList();

    /**
     * 名称。
     * <p>
     * 路由名称，用于 {@code <keep-alive>} 属性。
     * <p>
     * 一般情况下和路径保持一致，可能以首字母大写的单词形式存在，比如 path 路径变成 Path 名称。
     */
    @Size(max = 50)
    @Column(length = 50)
    private String name;
    /**
     * 路径。
     * <p>
     * 路由路径，拼接上级路径后，直接展示在浏览器中。
     * <p>
     * 通常可以不限制长度，但为了与名称保持一致，所以选择限制为名称的两倍长度。
     */
    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String path;
    /**
     * 完整路径。
     * <p>
     * 通常路径是一个相对路径，除顶级菜单外，其他路径都不包含路径标识符，即 '/' 符号。
     * <p>
     * 另外，如果不保留完整路径，则每次获取菜单数据时，都可能需要递归获取上级菜单路径进行拼接，效率比较差。
     * <p>
     * 但如果保留完整路径，则在修改上级菜单路径时，也要遍历子级菜单同步路径，否则将导致子级菜单失效。
     */
    @NotBlank
    @Size(max = 1024)
    @Column(nullable = false, length = 1024)
    private String fullPath;
    /**
     * 组件。
     * <p>
     * 前端的组件名称，或者前端的组件路径。
     * <p>
     * 注意：这个字段非常关键，如果设置错误，将无法正确展示页面。
     */
    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
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
    @Size(max = 1024)
    @Column(length = 1024)
    private String redirect;
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
     * 元数据。
     */
    @Embedded
    private MenuMeta meta;
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
    private Boolean enabled = true;

    /**
     * 菜单资源列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "menu", orphanRemoval = true)
    private List<MenuResource> resourceList = Lists.newArrayList();

    /**
     * 菜单对应的角色列表。
     * <p>
     * 主要可以返回给前端，当前菜单支持哪些角色。
     */
    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "menuList")
    private List<Role> roleList = Lists.newArrayList();

}
