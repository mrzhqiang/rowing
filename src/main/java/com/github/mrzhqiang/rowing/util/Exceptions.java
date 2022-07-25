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
     * 未知异常消息。
     * <p>
     * 作为异常消息的后备选择，通常不会返回此内容，除非异常实例为 null 值。
     */
    public static final String UNKNOWN_MESSAGE = "Unknown";

    /**
     * 异常痕迹。
     * <p>
     * 工具类只做纯粹的事情，是否处于调试模式，由调用方判断。
     *
     * @param exception 异常实例。
     * @return 异常痕迹堆栈信息。
     */
    public static String ofTrace(@Nullable Exception exception) {
        return Optional.ofNullable(exception)
                .map(Throwables::getStackTraceAsString)
                .orElse("");
    }

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
}
