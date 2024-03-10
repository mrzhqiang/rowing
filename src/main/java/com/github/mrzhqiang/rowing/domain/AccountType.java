package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Enums;
import org.springframework.security.core.GrantedAuthority;

/**
 * 账户类型。
 * <p>
 * 目前有三种类型：管理员、用户、游客。
 * <p>
 * 用于区分账户的访问范围：公共资源--游客，系统功能--用户，管理后台--管理员。
 * <p>
 * 作为内置角色，枚举值的长度不能超过 {@link Domains#ROLE_CODE_LENGTH} 设定的长度。
 */
public enum AccountType implements GrantedAuthority {

    /**
     * 管理员。
     */
    ADMIN,
    /**
     * 用户。
     */
    USER,
    /**
     * 游客。
     */
    ANONYMOUS,
    ;

    public static AccountType of(String type) {
        return Enums.findByNameIgnoreCase(AccountType.class, type, ANONYMOUS);
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }

}
