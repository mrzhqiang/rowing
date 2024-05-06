package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.SettingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 设置。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Setting extends AuditableEntity {

    private static final long serialVersionUID = 2749010192855132317L;

    /**
     * 类型。
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_LENGTH)
    private SettingType type;
    /**
     * 名称。
     */
    @NotBlank
    @Size(max = Domains.SETTING_NAME_LENGTH)
    @Column(nullable = false, length = Domains.SETTING_NAME_LENGTH)
    private String name;
    /**
     * 代码。
     */
    @NotBlank
    @Size(max = Domains.SETTING_CODE_LENGTH)
    @Column(nullable = false, unique = true, length = Domains.SETTING_CODE_LENGTH)
    private String code;
    /**
     * 内容。
     */
    @Size(max = Domains.SETTING_CONTENT_LENGTH)
    @Column(nullable = false, length = Domains.SETTING_CONTENT_LENGTH)
    private String content;
    /**
     * 风格。
     */
    @Size(max = Domains.SETTING_STYLE_LENGTH)
    @Column(length = Domains.SETTING_STYLE_LENGTH)
    private String style;

}
