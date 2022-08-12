package com.github.mrzhqiang.rowing.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 序列工具。
 */
public final class Sequences {
    private Sequences() {
        // no instances.
    }

    public static final DateTimeFormatter BASIC_DATE_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 基础日期时间序列。
     * <p>
     * 比如：202208121814。
     *
     * @return 日期时间序列字符串。
     */
    public static String ofBasicDateTime() {
        return LocalDateTime.now().format(BASIC_DATE_TIME);
    }

    /**
     * 基础日期序列。
     * <p>
     * 比如：20220812
     *
     * @return 日期序列字符串。
     */
    public static String ofBasicDate() {
        return LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }
}
