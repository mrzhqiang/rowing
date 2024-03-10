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
     */
    NONE,
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
    /**
     * 创建账户。
     */
    CREATE_ACCOUNT,
    /**
     * 创建菜单。
     */
    CREATE_MENU,
    ;

    public static ActionType of(String type) {
        return Enums.findByNameIgnoreCase(ActionType.class, type, NONE);
    }

}
