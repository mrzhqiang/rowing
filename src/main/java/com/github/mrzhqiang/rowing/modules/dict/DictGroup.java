package com.github.mrzhqiang.rowing.modules.dict;

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
    @Column(nullable = false)
    private String name;
    /**
     * 代码。
     * <p>
     * 可以用来查询相关字典项，因此必须保证全局唯一。
     */
    @Column(unique = true, nullable = false)
    private String code;
    /**
     * 类型。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DictType type;

    /**
     * 字典组所包含的字典项列表。
     * <p>
     * 字典项一般用来提供选择，比如下拉框、多选框等等。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "group")
    private List<DictItem> items;

}
