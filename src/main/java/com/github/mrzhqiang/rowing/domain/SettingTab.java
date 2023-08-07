package com.github.mrzhqiang.rowing.domain;

import java.util.stream.Stream;

/**
 * 设置选项。
 * <p>
 * 主要用于对相同类型的设置进行分组，方便以选项卡形式展示。
 */
public enum SettingTab {

    /**
     * 一般。
     * <p>
     * 最基本的设置，通常包含：网站标题、欢迎文字、网站作者设置等。
     */
    GENERAL,
    /**
     * 认证。
     * <p>
     * 与认证相关的设置，比如：自动登录（30天）、自动注册、密码规则等。
     */
    AUTHENTICATION,
    /**
     * 文件。
     * <p>
     * 与文件相关的设置，比如附件大小限制、允许上传的附件类型、附件名称编码规则等。
     */
    FILE,
    /**
     * 安全。
     * <p>
     * 与安全有关的设置，比如账户登录失败最大次数，账户锁定时长等。
     */
    SECURITY,
    ;

    public static SettingTab of(String tab) {
        return Stream.of(values())
                .filter(it -> it.name().equalsIgnoreCase(tab))
                .findFirst()
                .orElse(GENERAL);
    }
}
