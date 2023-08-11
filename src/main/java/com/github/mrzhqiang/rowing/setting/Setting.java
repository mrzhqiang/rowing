package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.SettingTab;
import com.github.mrzhqiang.rowing.domain.SettingType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 设置。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Setting extends AuditableEntity {

    private static final long serialVersionUID = 2749010192855132317L;

    /**
     * 标签。
     */
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String label;
    /**
     * 类型。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SettingType type;
    /**
     * 名称。
     */
    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    /**
     * 内容。
     */
    @Size(max = 5000)
    @Column(nullable = false, length = 5000)
    private String content;
    /**
     * 选项。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = MAX_ENUM_NAME_LENGTH)
    private SettingTab tab;
    /**
     * 风格。
     */
    @Size(max = 100)
    @Column(length = 100)
    private String style;

}
