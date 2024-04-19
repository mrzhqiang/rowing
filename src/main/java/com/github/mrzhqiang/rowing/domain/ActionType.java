package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Enums;

/**
 * 操作类型。
 * <p>
 * 通常情况下，应包含：注册、登录、注销等操作。
 */
public enum ActionType {

    /**
     * 空操作。
     * <p>
     * 记录操作日志时，将采用方法名称作为操作名称。
     */
    NONE,
    /**
     * 注册。
     */
    REGISTER,
    /**
     * 登录成功。
     */
    LOGIN_SUCCESSFUL,
    /**
     * 登录失败。
     */
    LOGIN_FAILURE,
    /**
     * 注销。
     */
    LOGOUT,
    /**
     * 创建菜单。
     */
    CREATE_MENU,
    ;

    public static ActionType of(String type) {
        return Enums.findByNameIgnoreCase(ActionType.class, type, NONE);
    }

}
