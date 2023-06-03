package com.github.mrzhqiang.rowing.domain;


import org.springframework.util.StringUtils;

/**
 * 判断逻辑。
 * <p>
 * 用于【是】或【否】的判断。
 */
public enum Logic {

    /**
     * 是。
     */
    YES,
    /**
     * 否。
     */
    NO,
    ;

    /**
     * 逻辑是。
     * <p>
     * 注意：这个方法忽略代码的大小写。
     *
     * @param code 逻辑代码。
     * @return 返回 true 表示代码对应的逻辑为是；否则返回 false 值。
     */
    public static boolean yes(String code) {
        return YES.name().equalsIgnoreCase(code);
    }

    /**
     * 逻辑否。
     * <p>
     * 如果代码为 null 或者包含任意空字符串，这个方法也视为逻辑否。
     * <p>
     * 注意：这个方法忽略代码的大小写。
     *
     * @param code 逻辑代码。
     * @return fanh true 表示代码对应的逻辑为否；否则返回 true 值。
     */
    public static boolean no(String code) {
        return !StringUtils.hasText(code) || NO.name().equalsIgnoreCase(code);
    }
}
