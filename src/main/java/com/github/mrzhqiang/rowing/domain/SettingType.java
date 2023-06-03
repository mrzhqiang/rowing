package com.github.mrzhqiang.rowing.domain;

import java.util.stream.Stream;

/**
 * 设置类型。
 * <p>
 * 主要用于声明设置对应的类型。
 */
public enum SettingType {

    /**
     * 输入框。
     */
    INPUT,
    /**
     * 下拉选择框。
     */
    SELECT,
    /**
     * 单选按钮。
     */
    RADIOS,
    /**
     * 多选按钮。
     */
    CHECKS,
    /**
     * 开关按钮。
     */
    SWITCHES,
    /**
     * 范围数值。
     */
    RANGE,
    /**
     * 文本区域。
     */
    AREA,
    ;

    public static SettingType of(String type) {
        return Stream.of(values())
                .filter(it -> it.name().equalsIgnoreCase(type))
                .findFirst()
                .orElse(INPUT);
    }
}
