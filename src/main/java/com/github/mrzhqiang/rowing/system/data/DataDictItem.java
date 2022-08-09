package com.github.mrzhqiang.rowing.system.data;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
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
     * <p>
     * 以 yyyyMMddHHmmss 格式作为序列号。
     * <p>
     * 通常序列号来自组序列号，除非是通过系统新增的字典项。
     */
    private String serialNo;

    /**
     * 字典组。
     * <p>
     * 当前字典项所在组。
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private DataDictGroup group;
}
