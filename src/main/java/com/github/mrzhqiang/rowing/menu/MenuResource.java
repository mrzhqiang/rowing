package com.github.mrzhqiang.rowing.menu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.role.Role;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 菜单资源。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class MenuResource extends AuditableEntity implements GrantedAuthority {

    private static final long serialVersionUID = -8856149065035259776L;

    /**
     * 资源名称最大长度。
     */
    public static final int MAX_NAME_LENGTH = 24;
    /**
     * 资源权限最大长度。
     */
    public static final int MAX_AUTHORITY_LENGTH = 100;

    /**
     * 资源名称。
     * <p>
     * 不能为空，最大长度 24 个字符。
     */
    @NotBlank
    @Size(max = MAX_NAME_LENGTH)
    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;
    /**
     * 资源权限。
     * <p>
     * 不能为空，必须保证唯一，最大长度 100 个字符。
     */
    @NotBlank
    @Size(max = MAX_AUTHORITY_LENGTH)
    @Column(unique = true, nullable = false, length = MAX_AUTHORITY_LENGTH)
    private String authority;
    /**
     * 资源排序。
     * <p>
     * 主要影响在菜单下的列表顺序，如果未设置的话，将以创建时间作为默认排序条件。
     */
    private Integer ordered;

    /**
     * 资源所属菜单。
     */
    @ManyToOne(optional = false)
    private Menu menu;
    /**
     * 资源所属角色列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "menuResourceList")
    private List<Role> roleList = Lists.newArrayList();

}
