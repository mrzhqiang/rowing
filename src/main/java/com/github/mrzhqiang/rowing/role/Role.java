package com.github.mrzhqiang.rowing.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.menu.Menu;
import com.github.mrzhqiang.rowing.menu.MenuResource;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.expression.SecurityExpressionRoot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 角色。
 * <p>
 * 角色可以视为一个账户分组，包含已授权的账户列表。
 * <p>
 * 同时，角色会分配相应的菜单列表和菜单资源列表，表示属于此角色的账户可访问哪些菜单和菜单资源。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Role extends AuditableEntity {

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
     * <p>
     * 通常格式为：ROLE_XXX 或者 XXX，{@link SecurityExpressionRoot#hasRole(String)} 方法会自动补充 ROLE_ 角色前缀。
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
    @ManyToMany()
    @JoinTable(name = "role_accounts",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "account_id"}))
    private List<Account> accounts = Lists.newArrayList();
    /**
     * 菜单列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @RestResource(path = "menus")
    @ManyToMany()
    @JoinTable(name = "role_menus",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "menu_id"}))
    private List<Menu> menus = Lists.newArrayList();
    /**
     * 菜单资源列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @RestResource(path = "menu-resources")
    @ManyToMany()
    @JoinTable(name = "role_menu_resources",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_resource_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "menu_resource_id"}))
    private List<MenuResource> menuResources = Lists.newArrayList();

}
