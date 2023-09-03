package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.SettingTab;
import com.github.mrzhqiang.rowing.domain.SettingType;
import com.github.mrzhqiang.rowing.domain.Domains;
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
    @Size(max = Domains.SETTING_LABEL_LENGTH)
    @Column(nullable = false, length = Domains.SETTING_LABEL_LENGTH)
    private String label;
    /**
     * 类型。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_NAME_LENGTH)
    private SettingType type;
    /**
     * 名称。
     */
    @NotBlank
    @Size(max = Domains.SETTING_NAME_LENGTH)
    @Column(nullable = false, unique = true, length = Domains.SETTING_NAME_LENGTH)
    private String name;
    /**
     * 内容。
     */
    @Size(max = Domains.SETTING_CONTENT_LENGTH)
    @Column(nullable = false, length = Domains.SETTING_CONTENT_LENGTH)
    private String content;
    /**
     * 选项。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_NAME_LENGTH)
    private SettingTab tab;
    /**
     * 风格。
     */
    @Size(max = Domains.SETTING_STYLE_LENGTH)
    @Column(length = Domains.SETTING_STYLE_LENGTH)
    private String style;

}
