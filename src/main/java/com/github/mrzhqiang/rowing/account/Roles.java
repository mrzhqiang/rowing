package com.github.mrzhqiang.rowing.account;

/**
 * 角色常量。
 */
public final class Roles {
    private Roles() {
        // no instances
    }

    /**
     * 角色前缀。
     */
    public static final String PREFIX = "ROLE_";

    /**
     * 原生管理员。
     */
    public static final String RAW_ADMIN = "ADMIN";
    /**
     * 管理员角色。
     */
    public static final String ADMIN = PREFIX + RAW_ADMIN;
    /**
     * 原生用户。
     */
    public static final String RAW_USER = "USER";
    /**
     * 用户角色。
     */
    public static final String USER = PREFIX + RAW_USER;

    /**
     * 等级制度。
     * <p>
     * 比如：管理员也是一个用户，所以用户有的权限，管理员也同样拥有。
     */
    public static final String HIERARCHY = ADMIN + " > " + USER;
}
