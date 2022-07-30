package com.github.mrzhqiang.rowing.core.system.exception;

import com.google.common.base.Strings;
import com.google.common.base.VerifyException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Optional;

/**
 * 异常代码。
 */
public enum ExceptionCode {
    /**
     * 默认类型。
     * <p>
     * 未分类的异常都属于默认异常，后续将通过日志记录进行分类。
     */
    DEFAULT("0"),
    /**
     * IO 异常。
     * <p>
     * 通常是数据库或网络连接出现问题。
     *
     * @see IOException
     */
    IO("1"),
    /**
     * 代码异常。
     * <p>
     * 通常是代码编写不规范导致。
     *
     * @see NullPointerException
     * @see IndexOutOfBoundsException
     * @see NoSuchFieldException
     * @see NoSuchMethodException
     * @see NumberFormatException
     * @see UnsupportedOperationException
     */
    CODE("2"),
    /**
     * 非法相关异常。
     * <p>
     * 通常是方法参数或程序状态异常导致。
     *
     * @see IllegalArgumentException
     * @see IllegalStateException
     * @see IllegalAccessException
     */
    ILLEGAL("3"),
    /**
     * 验证相关异常。
     * <p>
     * 通常是程序状态验证不通过导致。
     *
     * @see VerifyException
     */
    VERIFY("4"),
    /**
     * 运行时异常。
     * <p>
     * 通常是运行过程中出现的自定义异常，一般属于可控范围的问题。
     *
     * @see RuntimeException
     */
    RUNTIME("5"),
    ;

    /**
     * 需要填充的异常代码前缀。
     */
    private static final char PAD_CHAR = '0';
    /**
     * 异常代码类型的最小长度。
     * <p>
     * 如果这个值是 3 且传入数值是 1 ，则会格式化为 001 字符串，填充的字符由 PAD_CHAR 决定。
     */
    private static final int TYPE_MIN_LENGTH = 3;
    /**
     * 异常消息的取模范围。
     * <p>
     * 由于异常消息是未知的，但通过 hashCode 之后取模，可以得到一个固定范围的编码，
     * 相同的异常消息总能得到相同的编码，可以帮助我们分析程序中存在的各种异常，从而甄选出适合让用户知晓的错误类型。
     */
    private static final int MESSAGE_MOD = 10000;
    /**
     * 异常消息的最小长度。
     * <p>
     * 异常消息取模之后，可能存在低于 4 位的数字，此时需要对其进行格式化。
     * <p>
     * 比如： 101 格式化之后就变成 0101。
     * <p>
     * 相同格式的异常代码有利于快速检索。
     */
    private static final int MESSAGE_MIN_LENGTH = 4;
    /**
     * 异常代码的格式化模板。
     * <p>
     * 比如：404-001-1111 表示 404 http 状态，001 默认异常（可能未识别），1111 异常消息代码。
     */
    private static final String FORMAT_TEMPLATE = "%s-%s-%s";
    /**
     * 0 数字的字符串。
     */
    private static final String NUMBER_ZERO = Integer.toString(0);

    /**
     * 自定义代码类型。
     * <p>
     * 可以一直增加自定义类型，这个是大类，目前上限为 999 个，如果有更多的需求，再决定是否修改 TYPE_MIN_LENGTH 为 4 位数、5位数。
     */
    private final String type;

    ExceptionCode(String type) {
        this.type = type;
    }

    /**
     * 根据具体异常返回异常代码。
     *
     * @param exception 具体异常。
     * @return 异常代码。
     */
    public static ExceptionCode of(@Nullable Exception exception) {
        if (exception == null) {
            return DEFAULT;
        }

        if (exception instanceof IOException) {
            return IO;
        }

        if (exception instanceof NullPointerException
                || exception instanceof IndexOutOfBoundsException
                || exception instanceof NoSuchFieldException
                || exception instanceof NoSuchMethodException
                || exception instanceof NumberFormatException
                || exception instanceof UnsupportedOperationException) {
            return CODE;
        }

        if (exception instanceof IllegalArgumentException
                || exception instanceof IllegalStateException
                || exception instanceof IllegalAccessException) {
            return ILLEGAL;
        }

        if (exception instanceof VerifyException) {
            return VERIFY;
        }

        if (exception instanceof RuntimeException) {
            return RUNTIME;
        }
        return DEFAULT;
    }

    /**
     * 格式化异常代码。
     * <p>
     * 1. 以 http code 为前缀，比如：4xx 客户端异常、5xx 服务器异常
     * <p>
     * 2. 以异常类型为中缀，最小 3 位长度不足补零，比如：4xx-000 客户端-默认类型
     * <p>
     * 3. 以具体异常为后缀，暂时从 hash code 取 10000 的 floorMod 进行拼接，不足补零
     * <p>
     * 最终的异常代码为：4xx-000-xxxx 客户端-默认类型-具体异常
     * <p>
     * 鉴于具体异常以 String 的 hash code 为准，因此必须输出内容，不能因为国际化而导致同一个异常不同的输出语言。
     * <p>
     * 为此，我们在编写异常消息时，直接通过中文表达即可。
     *
     * @param httpStatus HTTP 状态码
     * @param message    异常消息，如果为 Null 则默认为 000 中缀。
     * @return 格式化的异常代码字符串，主要是 xxx-xxx-xxxx 格式。
     */
    public String format(int httpStatus, @Nullable String message) {
        String infix = Strings.padStart(type, TYPE_MIN_LENGTH, PAD_CHAR);

        String suffix = Optional.ofNullable(message)
                .map(String::hashCode)
                // floorMod 方法返回与第二个参数 y 相同符号的数值，即 y 为正数则返回正数，y 为负数返回负数
                // x 的正负符号只会影响结果的数值，不会影响结果的符号
                // 比如 x=1 y=10000 返回 1，而 x=-1 y=10000 返回 9999
                // 因此 suffix 将不会出现负数，也就不需要使用绝对值
                .map(it -> Math.floorMod(it, MESSAGE_MOD))
                .map(String::valueOf)
                .orElse(NUMBER_ZERO);
        suffix = Strings.padStart(suffix, MESSAGE_MIN_LENGTH, PAD_CHAR);

        return Strings.lenientFormat(FORMAT_TEMPLATE, httpStatus, infix, suffix);
    }
}
