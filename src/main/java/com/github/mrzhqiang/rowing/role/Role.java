package com.github.mrzhqiang.rowing.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.menu.Menu;
import com.github.mrzhqiang.rowing.menu.MenuResource;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 角色。
 * <p>
 * 角色可以视为一个分组，包含菜单及菜单资源列表，也包含账户列表。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Role extends AuditableEntity implements GrantedAuthoritiesContainer {

    private static final long serialVersionUID = -2682925657637319638L;

    /**
     * 角色名称。
     */
    @NotBlank
    @Size(max = Domains.ROLE_NAME_LENGTH)
    @Column(unique = true, nullable = false, length = Domains.ROLE_NAME_LENGTH)
    private String name;
    /**
     * 角色代码。
     */
    @NotBlank
    @Size(max = Domains.ROLE_CODE_LENGTH)
    @Column(unique = true, nullable = false, length = Domains.ROLE_CODE_LENGTH)
    private String code;
    /**
     * 是否不可变。
     * <p>
     * 不可变的角色属于内置角色，即 {@link com.github.mrzhqiang.rowing.domain.AccountType 账户类型}。
     */
    private Boolean immutable = false;

    /**
     * 账户列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @RestResource(path = "accounts")
    @ManyToMany(mappedBy = "roles")
    private List<Account> accounts = Lists.newArrayList();
    /**
     * 菜单列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @RestResource(path = "menus")
    @ManyToMany()
    @JoinTable(name = "role_menus",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "menu_id"}))
    private List<Menu> menus = Lists.newArrayList();
    /**
     * 菜单资源列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @RestResource(path = "menu-resources")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_menu_resources",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "menu_resource_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "menu_resource_id"}))
    private List<MenuResource> menuResources = Lists.newArrayList();

    @Override
    public Collection<GrantedAuthority> getGrantedAuthorities() {
        return Stream.concat(Stream.of(code), menuResources.stream().map(MenuResource::getAuthority))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
