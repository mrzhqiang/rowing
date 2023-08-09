package com.github.mrzhqiang.rowing.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.stream.Stream;

/**
 * 账户类型。
 * <p>
 * 目前有三种类型：管理员、用户、游客。
 * <p>
 * 用于区分账户的访问范围：公共资源--游客，系统功能--用户，管理后台--管理员。
 */
public enum AccountType {

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

    /**
     * 字符串转账户类型。
     *
     * @param type 类型字符串。
     * @return 账户类型。
     */
    public static AccountType of(String type) {
        if (!StringUtils.hasText(type)) {
            return ANONYMOUS;
        }

        return Stream.of(values())
                .filter(it -> type.equals(it.name()))
                .findFirst()
                .orElse(ANONYMOUS);
    }

    /**
     * 账户类型转为授权。
     *
     * @return 授权。
     */
    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(name());
    }

}
