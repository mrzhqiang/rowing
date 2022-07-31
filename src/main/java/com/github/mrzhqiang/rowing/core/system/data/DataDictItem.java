package com.github.mrzhqiang.rowing.core.system.data;

import com.github.mrzhqiang.rowing.core.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * 字典项。
 * <p>
 * 字典项是具体的字典数据，通过字典组进行分类。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class DataDictItem extends AuditableEntity {

    /**
     * 图标。
     * <p>
     * 这个字段是可选的，用来在 label 前面显示图标。
     */
    private String icon;
    /**
     * 标签。
     * <p>
     * 字典项的名称，用于展示。
     */
    @Column(nullable = false)
    private String label;
    /**
     * 值。
     * <p>
     * 字典项的值，用于查询。
     */
    @Column(nullable = false)
    private String value;
    /**
     * 序列号。
     */
    private String serialNo;

    /**
     * 分组。
     * <p>
     * 当前字典项所在分组。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private DataDictGroup group;
}
