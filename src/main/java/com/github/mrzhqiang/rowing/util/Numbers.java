package com.github.mrzhqiang.rowing.util;

import com.google.common.base.Strings;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.math.BigDecimal;

/**
 * 数字工具。
 */
public final class Numbers {
    private Numbers() {
        // no instances
    }

    /**
     * 整型数字。
     * <p>
     * 如果字符串源为 null 或空串，则默认返回 null 值。
     *
     * @param source 字符串源。
     * @return 字符串源转换的整型数字，可能会返回 null 值。
     */
    @Nullable
    public static Integer ofInteger(String source) {
        return ofInteger(source, null);
    }

    /**
     * 整型数字。
     *
     * @param source       字符串源。
     * @param defaultValue 默认值。
     * @return 字符串源转换的整型数字，如果字符串源为 null 值或空串，则返回默认值。
     */
    @Nullable
    public static Integer ofInteger(String source, @Nullable Integer defaultValue) {
        if (!StringUtils.hasText(source)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(source);
        } catch (Exception ignored) {
        }
        return defaultValue;
    }

    /**
     * 长整型数字。
     * <p>
     * 如果字符串源为 null 或空串，则默认返回 null 值。
     *
     * @param source 字符串源。
     * @return 字符串源转换的长整型数字，可能会返回 null 值。
     */
    @Nullable
    public static Long ofLong(String source) {
        return ofLong(source, null);
    }

    /**
     * 长整型数字。
     *
     * @param source       字符串源。
     * @param defaultValue 默认值。
     * @return 字符串源转换的长整型数字，如果字符串源为 null 值或空串，则返回默认值。
     */
    @Nullable
    public static Long ofLong(String source, @Nullable Long defaultValue) {
        if (!StringUtils.hasText(source)) {
            return defaultValue;
        }

        try {
            return Long.parseLong(source);
        } catch (Exception ignored) {
        }
        return defaultValue;
    }

    /**
     * 单精度浮点数。
     * <p>
     * 如果字符串源为 null 或空串，则默认返回 null 值。
     *
     * @param source 字符串源。
     * @return 字符串源转换的单精度浮点数，可能会返回 null 值。
     */
    @Nullable
    public static Float ofFloat(String source) {
        return ofFloat(source, null);
    }

    /**
     * 单精度浮点数。
     *
     * @param source       字符串源。
     * @param defaultValue 默认值。
     * @return 字符串源转换的单精度浮点数，如果字符串源返回 null 值或空串，则返回默认值。
     */
    @Nullable
    public static Float ofFloat(String source, @Nullable Float defaultValue) {
        if (!StringUtils.hasText(source)) {
            return defaultValue;
        }

        try {
            return Float.parseFloat(source);
        } catch (Exception ignored) {
        }
        return defaultValue;
    }

    /**
     * 双精度浮点数。
     * <p>
     * 如果字符串源为 null 或空串，则默认返回 null 值。
     *
     * @param source 字符串源，
     * @return 字符串源转换的双精度浮点数，可能会返回 null 值。
     */
    @Nullable
    public static Double ofDouble(String source) {
        return ofDouble(source, null);
    }

    /**
     * 双精度浮点数。
     *
     * @param source       字符串源。
     * @param defaultValue 默认值。
     * @return 字符串源转换的双精度浮点数，如果字符串源为 null 值或空串，则返回默认值。
     */
    @Nullable
    public static Double ofDouble(String source, @Nullable Double defaultValue) {
        if (Strings.isNullOrEmpty(source)) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(source);
        } catch (Exception ignored) {
        }
        return defaultValue;
    }

    /**
     * 大十进制数字。
     * <p>
     * 如果字符串源为 null 或空串，则默认返回 null 值。
     *
     * @param source 字符串源。
     * @return 字符串源转换的大十进制数字，如果字符串源为 null 值或空串，则返回默认值。
     */
    @Nullable
    public static BigDecimal ofBigDecimal(String source) {
        return ofBigDecimal(source, null);
    }

    /**
     * 大十进制数字。
     *
     * @param source       字符串源。
     * @param defaultValue 默认值。
     * @return 字符串源转换的大十进制数字，如果字符串源为 null 值或空串，则返回默认值。
     */
    @Nullable
    public static BigDecimal ofBigDecimal(String source, @Nullable BigDecimal defaultValue) {
        if (Strings.isNullOrEmpty(source)) {
            return defaultValue;
        }

        try {
            return new BigDecimal(source);
        } catch (Exception ignored) {
        }
        return defaultValue;
    }
}
