package com.github.mrzhqiang.rowing.core.data;

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
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class DataDictItem extends AuditableEntity {

    /**
     * 图标。
     * <p>
     * 允许为空，说明字典项不存在前置图标。
     * <p>
     * 图标一般以图标库引用名称为主，通常直接使用即可生效。
     */
    private String icon;
    @Column(nullable = false)
    private String label;
    @Column(nullable = false)
    private String value;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private DataDictGroup group;
}
