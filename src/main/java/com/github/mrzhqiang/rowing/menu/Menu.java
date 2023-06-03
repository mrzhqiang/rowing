package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.entity.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.MenuType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
     * 名称。
     */
    @Column(nullable = false, length = 100)
    private String name;
    /**
     * 编号。
     */
    @Column(nullable = false, length = 50)
    private String code;
    /**
     * 路径。
     */
    @Column(nullable = false)
    private String path;
    /**
     * 类型。
     */
    @Column(nullable = false, length = 50)
    private MenuType type;
    /**
     * 图标。
     */
    private String icon;
    /**
     * 排序。
     */
    private Integer ordered;

    /**
     * 上级菜单。
     */
    @ManyToOne
    @JoinColumn(name = "parent_code", referencedColumnName = "code",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Menu parent;

    /**
     * 下级子菜单。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "parent")
    private List<Menu> children;

}
