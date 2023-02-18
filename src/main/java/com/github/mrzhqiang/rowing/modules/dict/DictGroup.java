package com.github.mrzhqiang.rowing.modules.dict;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.DictType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * 数据字典组。
 * <p>
 * 聚合同一类型的数据字典到分组中，从而只需要提供组代码，即可获得相关的所有数据字典项。
 * <p>
 * 比如名为性别的数据字典组，代码为 gender，内置类型，包含：未知 UNKNOWN、女 FEMALE、男 MALE 三项数据字典。
 * <p>
 * 前端只需要传参 code=gender 即可获得性别所包含的三项数据字典。
 * <p>
 * 这样就允许存在多个数据字典，其名称相同而值不同。
 * <p>
 * 需要注意的是，在同一个数据字典组内，不允许值相同的数据字典项，对于名称相同的字典项，由管理员自行把控，代码不做控制。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class DictGroup extends AuditableEntity {

    /**
     * 字典组名称。
     * <p>
     * 组名称通常用于界面展示。
     */
    @Column(nullable = false)
    private String name;
    /**
     * 字典组代码。
     * <p>
     * 组代码可以用来查询相关字典项，因此必须保证全局唯一。
     */
    @Column(unique = true, nullable = false)
    private String code;
    /**
     * 字典组类型。
     * <p>
     * 内置类型表示在代码中直接使用的枚举字典；自定义类型是用户自己维护的数据字典。
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
