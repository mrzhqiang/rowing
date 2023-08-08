package com.github.mrzhqiang.rowing.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.menu.Menu;
import com.github.mrzhqiang.rowing.menu.MenuResource;
import com.github.mrzhqiang.rowing.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.compress.utils.Lists;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 角色。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Role extends AuditableEntity {

    /**
     * 名称。
     * <p>
     * 唯一，必填，最大长度 24 个字符。
     */
    @NotBlank
    @Size(max = 24)
    @Column(unique = true, nullable = false, length = 24)
    private String name;
    /**
     * 代码。
     * <p>
     * 唯一，必填，最大长度 48 个字符。
     */
    @NotBlank
    @Size(max = 48)
    @Column(unique = true, nullable = false, length = 48)
    private String code;

    /**
     * 角色菜单列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "role_menus",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id"))
    private List<Menu> menuList = Lists.newArrayList();
    /**
     * 角色资源列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "role_resources",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"))
    private List<MenuResource> resourceList = Lists.newArrayList();

    /**
     * 角色所属用户列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "roleList")
    private List<User> userList = Lists.newArrayList();

}
