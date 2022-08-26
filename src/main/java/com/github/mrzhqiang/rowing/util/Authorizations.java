package com.github.mrzhqiang.rowing.util;

/**
 * 身份授权工具。
 * <p>
 * 主要提供简便的授权表达式，避免编写错误带来的异常。
 */
public final class Authorizations {
    private Authorizations() {
        // no instances
    }

    /**
     * 角色授权前缀。
     */
    public static final String HAS_ROLE_PREFIX = "hasRole('";
    /**
     * 角色授权后缀。
     */
    public static final String HAS_ROLE_SUFFIX = "')";
    /**
     * 管理员角色授权。
     */
    public static final String HAS_ROLE_ADMIN = HAS_ROLE_PREFIX + "ADMIN" + HAS_ROLE_SUFFIX;
    /**
     * 普通用户角色授权。
     */
    public static final String HAS_ROLE_USER = HAS_ROLE_PREFIX + "USER" + HAS_ROLE_SUFFIX;
    /**
     * 匿名用户角色授权。
     */
    public static final String HAS_ROLE_ANONYMOUS = HAS_ROLE_PREFIX + "ANONYMOUS" + HAS_ROLE_SUFFIX;

}
