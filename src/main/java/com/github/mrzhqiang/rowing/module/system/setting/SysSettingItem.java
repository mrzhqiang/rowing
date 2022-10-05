package com.github.mrzhqiang.rowing.module.system.setting;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 系统设置项。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class SysSettingItem extends AuditableEntity {

    @Column(nullable = false)
    private String label;
    @Column(nullable = false)
    private String name;
    private String value;

    @ToString.Exclude
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private SysSettingGroup group;
}
