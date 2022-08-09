package com.github.mrzhqiang.rowing.system.setting;

import com.github.mrzhqiang.rowing.api.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * 系统设置项。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class SysSettingItem extends AuditableEntity {

    /**
     * 标签。
     */
    private String label;
    /**
     * 名称。
     */
    private String name;
    /**
     * 数值。
     */
    private String value;

    /**
     * 所属组。
     */
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private SysSettingGroup group;
}
