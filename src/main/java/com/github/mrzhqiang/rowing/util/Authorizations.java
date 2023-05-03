package com.github.mrzhqiang.rowing.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * 授权工具。
 * <p>
 * 主要提供简便的授权表达式，避免编写错误带来的异常。
 */
public final class Authorizations {
    private Authorizations() {
        // no instances
    }

    /**
     * 角色前缀。
     * <p>
     * 这个前缀是 Spring Security 框架的默认角色前缀。
     */
    public static final String ROLE_PREFIX = "ROLE_";

    /**
     * 管理员角色授权。
     */
    public static final String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";
    /**
     * 普通用户角色授权。
     */
    public static final String HAS_ROLE_USER = "hasRole('ROLE_USER')";
    /**
     * 匿名用户角色授权。
     */
    public static final String HAS_ROLE_ANONYMOUS = "hasRole('ROLE_ANONYMOUS')";

    /**
     * 角色的权限。
     *
     * @param role 指定角色。
     * @return 带角色前缀的权限。
     */
    public static SimpleGrantedAuthority ofRole(String role) {
        // 角色可以不带前缀，但 GrantedAuthority 必须携带前缀
        return new SimpleGrantedAuthority(ROLE_PREFIX + role);
    }
}
