package com.github.mrzhqiang.rowing.core.data;

import com.github.mrzhqiang.rowing.core.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.core.domain.DataDictType;
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
 * 需要注意的是，在同一个数据字典组内，不允许名称相同或值相同的数据字典项。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class DataDictGroup extends AuditableEntity {

    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String code;
    @Enumerated(EnumType.STRING)
    private DataDictType type;

    @ToString.Exclude
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<DataDictItem> items;
}
