package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Enums;

/**
 * 设置选项。
 * <p>
 * 主要用于对相同类型的设置进行分组，方便以选项卡形式展示。
 */
public enum SettingType {

    /**
     * 一般。
     * <p>
     * 通用设置。
     */
    GENERAL,
    /**
     * 认证。
     * <p>
     * 与账户认证相关的设置。
     */
    AUTHENTICATION,
    /**
     * 文件。
     * <p>
     * 与文件相关的设置。
     */
    FILE,
    /**
     * 安全。
     * <p>
     * 与安全有关的设置，比如账户登录失败最大次数，账户锁定时长等。
     */
    SECURITY,
    ;

    public static SettingType of(String type) {
        return Enums.findByNameIgnoreCase(SettingType.class, type, GENERAL);
    }

}
