package com.github.mrzhqiang.rowing.util;

import com.github.mrzhqiang.helper.random.RandomStrings;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 序列工具。
 */
public final class Sequences {
    private Sequences() {
        // no instances.
    }

    /**
     * UID 哈希取模。
     */
    public static final int UID_HASHCODE_MOD = 1000;
    /**
     * UID 后缀最小长度。
     */
    public static final int UID_SUFFIX_MIN_LENGTH = 3;
    /**
     * UID 后缀最大长度。
     */
    public static final int UID_SUFFIX_MAX_LENGTH = 5;
    /**
     * UID 序列最小长度。
     */
    public static final int UID_MIN_LENGTH = 7;
    /**
     * UID 不满足最小长度时的填充字符。
     */
    public static final char UID_PAD_CHAR = '0';
    /**
     * UID 参数键。
     */
    public static final String UID_KEY = "uid";

    /**
     * 基础的日期时间格式化器。
     * <p>
     * 主要用于序列号。
     */
    public static final DateTimeFormatter BASIC_DATE_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

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
     * 通过指定字符串生成 UID 序列的方法。
     * <p>
     * 比如：3425101。
     *
     * @param source 指定字符串，不能为 null 值。
     * @return uid 序列。
     */
    public static String ofUid(String source) {
        Preconditions.checkNotNull(source, "source == null");
        // 参考：HashMap.hash(obj) 由高 16 位组成 code 避免高频率的哈希冲撞
        int h;
        int code = (h = Objects.hashCode(source)) ^ (h >>> 16);
        // Math.floorMod 方法返回的数值符号由 y 参数符号决定，因此这里永远不会返回负数，符合预期
        int floorMod = Math.floorMod(code, UID_HASHCODE_MOD);

        // 生成长度为 min 及 max 之间的随机数字后缀
        String suffix = RandomStrings.ofNumber(UID_SUFFIX_MIN_LENGTH, UID_SUFFIX_MAX_LENGTH);
        String uid = floorMod + suffix;
        // 保证最小长度为 7 位，不足 7 位尾部补零
        uid = Strings.padEnd(uid, UID_MIN_LENGTH, UID_PAD_CHAR);
        return uid;
    }
}
