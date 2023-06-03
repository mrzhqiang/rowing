package com.github.mrzhqiang.rowing.domain;

import java.util.stream.Stream;

/**
 * 设置分组。
 * <p>
 * 主要用于对相同类型的设置进行分组。
 */
public enum SettingGroup {

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
    ;

    public static SettingGroup of(String group) {
        return Stream.of(values())
                .filter(it -> it.name().equalsIgnoreCase(group))
                .findFirst()
                .orElse(GENERAL);
    }
}
