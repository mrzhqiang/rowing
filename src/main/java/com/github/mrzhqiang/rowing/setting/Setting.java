package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.SettingGroup;
import com.github.mrzhqiang.rowing.domain.SettingType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 设置。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Setting extends AuditableEntity {

    /**
     * 标签。
     */
    @Column(nullable = false, length = 100)
    private String label;
    /**
     * 类型。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SettingType type;
    /**
     * 风格。
     */
    private String style;
    /**
     * 名称。
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    /**
     * 数值。
     */
    @Column(nullable = false)
    private String value;
    /**
     * 分组。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SettingGroup group;

}
