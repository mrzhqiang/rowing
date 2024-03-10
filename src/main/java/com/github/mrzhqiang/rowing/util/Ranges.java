package com.github.mrzhqiang.rowing.util;

import lombok.experimental.UtilityClass;

/**
 * 范围工具。
 * <p>
 */
@UtilityClass
public class Ranges {

    /**
     * 检测值是否在范围内。
     *
     * @param value 数值。
     * @param min   最小值。
     * @param max   最大值。
     * @return 返回 true 表示数值在最小值和最大值之间，包括最小值和最大值。
     */
    public static boolean in(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * 检测值是否在范围内。
     *
     * @param value 数值。
     * @param min   最小值。
     * @param max   最大值。
     * @return 返回 true 表示数值在最小值和最大值之间，包括最小值，但不包括最大值。
     */
    public static boolean inLeft(int value, int min, int max) {
        return value >= min && value < max;
    }

    /**
     * 检测值是否在范围内。
     *
     * @param value 数值。
     * @param min   最小值。
     * @param max   最大值。
     * @return 返回 true 表示数值在最小值和最大值之间，不包括最小值，但包括最大值。
     */
    public static boolean inRight(int value, int min, int max) {
        return value > min && value <= max;
    }

    /**
     * 检测值是否属于某个数组。
     *
     * @param value  数值。
     * @param values 数组。
     * @return 返回 true 表示数值属于后面的数组。
     */
    public static boolean oneOf(int value, int... values) {
        for (int v : values) {
            if (v == value) {
                return true;
            }
        }
        return false;
    }

}
