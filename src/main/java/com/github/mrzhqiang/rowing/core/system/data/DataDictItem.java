package com.github.mrzhqiang.rowing.core.system.data;

import com.github.mrzhqiang.rowing.core.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * 字典项。
 * <p>
 * 字典项是具体的字典数据，通过字典分组进行分类。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class DataDictItem extends AuditableEntity {

    /**
     * 标签。
     * <p>
     * 字典项的名称，用于展示。
     */
    private String label;
    /**
     * 值。
     * <p>
     * 字典项的值，用于查询。
     */
    private String value;

    /**
     * 分组。
     * <p>
     * 当前字典项所在分组。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private DataDictGroup group;
}
