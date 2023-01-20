package com.github.mrzhqiang.rowing.modules.setting;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * 系统设置组。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class SysSettingGroup extends AuditableEntity {

    /**
     * 名称。
     */
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String code;
    /**
     * 设置项。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<SysSettingItem> items;
}
