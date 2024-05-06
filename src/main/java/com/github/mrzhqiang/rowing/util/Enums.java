package com.github.mrzhqiang.rowing.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

/**
 * 枚举工具。
 * <p>
 * 定义枚举相关的常量和静态方法。
 */
@UtilityClass
public class Enums {

    /**
     * 通过名称找到枚举值。
     * <p>
     * 名称完全匹配，如果需要忽略大小写，请使用 {@link #findByNameIgnoreCase(Class, String, Enum)} 方法。
     *
     * @param enumClass 枚举类。
     * @param name      名称。
     * @param defEnum   默认的枚举类。
     * @return 枚举值。
     */
    public static <E extends Enum<E>> E findByName(Class<E> enumClass, String name, E defEnum) {
        if (StringUtils.hasText(name)) {
            for (E e : enumClass.getEnumConstants()) {
                // 完全匹配
                if (e.name().equals(name)) {
                    return e;
                }
            }
        }
        // 空值或者未匹配时，返回默认枚举值
        return defEnum;
    }

    /**
     * 通过名称找到枚举值。
     * <p>
     * 名称忽略大小写匹配，如果需要完全匹配，请使用 {@link #findByName(Class, String, Enum)} 方法。
     *
     * @param enumClass 枚举类。
     * @param name      名称。
     * @param defEnum   默认的枚举类。
     * @return 枚举值。
     */
    public static <E extends Enum<E>> E findByNameIgnoreCase(Class<E> enumClass, String name, E defEnum) {
        if (StringUtils.hasText(name)) {
            for (E e : enumClass.getEnumConstants()) {
                // 忽略大小写匹配
                if (e.name().equalsIgnoreCase(name)) {
                    return e;
                }
            }
        }
        // 空值或者未匹配时，返回默认枚举值
        return defEnum;
    }

}
