package com.github.mrzhqiang.rowing.domain;

/**
 * 操作类型。
 * <p>
 * 通常情况下，应包含：访问首页、注册、登录、注销等操作。
 */
public enum ActionType {
    /**
     * 空操作
     */
    NONE,
    /**
     * 访问首页。
     */
    MAIN,
    /**
     * 注册。
     */
    REGISTER,
    /**
     * 登录。
     */
    LOGIN,
    /**
     * 注销。
     */
    LOGOUT,
}
