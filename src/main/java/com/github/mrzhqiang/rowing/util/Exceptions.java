package com.github.mrzhqiang.rowing.util;

import com.google.common.base.Throwables;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * 异常工具。
 */
public final class Exceptions {
    private Exceptions() {
        // no instances.
    }

    /**
     * 最大异常痕迹字符串的长度。
     * <p>
     * 主要节省数据库存储空间，明确存储字段的长度。
     */
    public static final int MAX_TRACE_LENGTH = 2000;

    /**
     * 未知异常消息。
     * <p>
     * 作为异常消息的后备选择，通常不会返回此内容，除非异常实例为 null 值。
     */
    public static final String UNKNOWN_MESSAGE = "未知错误";

    /**
     * 异常消息。
     *
     * @param exception 异常实例。
     * @return 异常消息字符串，不会为 null 值。
     */
    public static String ofMessage(@Nullable Exception exception) {
        return Optional.ofNullable(exception)
                .map(Exception::getMessage)
                .orElse(UNKNOWN_MESSAGE);
    }

    /**
     * 异常痕迹。
     * <p>
     * 工具类只做纯粹的事情，是否处于调试模式，由调用方判断。
     * <p>
     * 为了方便持久化，将限制此方法返回的字符串长度。
     *
     * @param exception 异常实例。
     * @return 异常痕迹堆栈信息。
     */
    public static String ofTrace(@Nullable Exception exception) {
        return Optional.ofNullable(exception)
                .map(Throwables::getStackTraceAsString)
                .map(it -> it.substring(0, Math.min(MAX_TRACE_LENGTH, it.length())))
                .orElse("");
    }
}
