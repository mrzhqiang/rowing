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
 * 字典项是具体的字典数据，在不同的字典组下，支持相同标签甚至相同值的字典项。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class DataDictItem extends AuditableEntity {

    private String icon;
    @Column(nullable = false)
    private String label;
    @Column(nullable = false)
    private String value;

    /**
     * 字典组。
     * <p>
     * 当前字典项所在组。
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private DataDictGroup group;
}
