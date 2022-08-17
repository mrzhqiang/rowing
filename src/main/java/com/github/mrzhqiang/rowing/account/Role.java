package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

/**
 * 角色。
 * <p>
 * 基于 Spring Security 的角色体系，由于前后端分离，因此这里需要保持足够简单。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Role extends AuditableEntity {

    /**
     * 名称。
     * <p>
     * 即类似 ROLE_XXX 字符串，必须保证包含 ROLE_ 前缀，因为我们使用 AuthorityUtils.createAuthorityList 创建授权对象。
     */
    private String name;
    /**
     * 标签。
     * <p>
     * 用于中文显示。
     */
    private String label;
}
