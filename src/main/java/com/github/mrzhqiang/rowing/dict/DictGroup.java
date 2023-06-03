package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.DictType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * 字典组。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class DictGroup extends AuditableEntity {

    /**
     * 名称。
     * <p>
     * 通常用于界面展示。
     */
    @Column(nullable = false, length = 100)
    private String name;
    /**
     * 代码。
     * <p>
     * 可以用来查询相关字典项，因此必须保证全局唯一。
     */
    @Column(unique = true, nullable = false, length = 50)
    private String code;
    /**
     * 类型。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DictType type;
    /**
     * 冻结。
     * <p>
     * 冻结的字典不允许更新，包括初始化同步以及后台编辑时的操作。
     */
    @Enumerated(EnumType.STRING)
    private Logic freeze;

    /**
     * 字典组所包含的字典项列表。
     * <p>
     * 字典项一般用来提供选择，比如下拉框、多选框等等。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "group")
    private List<DictItem> items;

}
