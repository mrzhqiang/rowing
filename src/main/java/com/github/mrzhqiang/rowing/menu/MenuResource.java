package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 菜单资源。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class MenuResource extends AuditableEntity {

    private static final long serialVersionUID = -8856149065035259776L;

    /**
     * 资源名称。
     */
    @NotBlank
    @Size(max = Domains.MAX_NAME_LENGTH)
    @Column(nullable = false, length = Domains.MAX_NAME_LENGTH)
    private String name;
    /**
     * 资源代码。
     */
    @NotBlank
    @Size(max = Domains.MAX_AUTHORITY_LENGTH)
    @Column(unique = true, nullable = false, length = Domains.MAX_AUTHORITY_LENGTH)
    private String code;
    /**
     * 资源排序。
     * <p>
     * 主要影响在菜单下的列表顺序，如果未设置的话，将以创建时间作为默认排序条件。
     */
    @NotNull
    @Column(nullable = false)
    private Integer ordered = 0;
    /**
     * 资源所属菜单。
     */
    @ManyToOne(optional = false)
    private Menu menu;

}
