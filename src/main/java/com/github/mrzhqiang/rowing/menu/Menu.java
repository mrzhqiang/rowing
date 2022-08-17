package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Menu extends AuditableEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String path;
    private Type type;
    private String icon;

    @ManyToOne
    private Menu parent;

    @ToString.Exclude
    @OneToMany(mappedBy = "parent")
    private List<Menu> children;

    /**
     * 菜单类型。
     * <p>
     * 目前有 6 种菜单类型：
     * <p>
     * 1. 导航菜单组：对导航菜单进行分组
     * <p>
     * 2. 导航菜单项：可以被包含在菜单组中，也可以单独作为菜单
     * <p>
     * 3. 侧边菜单组：上级菜单必须是导航菜单项，对侧边菜单进行分组
     * <p>
     * 4. 侧边菜单项：可以被包含在菜单组中，也可以单独作为菜单
     * <p>
     * 5. 标签菜单：上级菜单必须是侧边菜单项，对内容进行分组
     * <p>
     * 6. （暂未使用）普通菜单：这个还没想好什么地方需要，暂时罗列出来。
     */
    public enum Type {
        /**
         * 导航菜单组。
         */
        NAV_GROUP,
        /**
         * 导航菜单项。
         */
        NAV_ITEM,
        /**
         * 侧边菜单组。
         */
        SIDE_GROUP,
        /**
         * 侧边菜单项。
         */
        SIDE_ITEM,
        /**
         * 标签菜单。
         */
        TAB,
        /**
         * 普通菜单。
         */
        NORMAL,
    }
}
