package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
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
     * 父级菜单。
     */
    @ManyToOne()
    private Menu parent;
    /**
     * 子级菜单。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "parent")
    private List<Menu> children = Lists.newArrayList();

    /**
     * 名称。
     */
    @NotBlank
    @Size(max = 32)
    @Column(nullable = false, length = 32)
    private String name;
    /**
     * 路径。
     */
    @NotBlank
    @Size(max = 32)
    @Column(nullable = false, length = 32)
    private String path;
    /**
     * 组件。
     */
    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String component;
    /**
     * 重定向。
     */
    @Size(max = 200)
    @Column(length = 200)
    private String redirect;
    /**
     * 标题。
     */
    @Size(max = 16)
    @Column(length = 16)
    private String title;
    /**
     * 图标。
     */
    @Size(max = 50)
    @Column(length = 50)
    private String icon;
    /**
     * 排序。
     */
    private Integer ordered;

}
