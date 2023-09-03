package com.github.mrzhqiang.rowing.util;

import com.github.mrzhqiang.helper.Exceptions;
import com.github.mrzhqiang.rowing.init.InitializationException;
import com.google.common.base.Strings;
import okhttp3.HttpUrl;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Set;

/**
 * 验证工具。
 */
public final class Validations {
    private Validations() {
        // no instances.
    }

    /**
     * 有效的 URL 地址。
     *
     * @param url URL 地址字符串。
     * @return 返回 true 表示传入参数为 URL 地址；返回 false 则表示不是。
     */
    public static boolean validUrl(String url) {
        return StringUtils.hasText(url) && HttpUrl.parse(url) != null;
    }

    /**
     * 从异常中找到消息。
     * <p>
     * 注意：如果是验证框架相关的异常，则获取验证失败的第一条消息返回；否则直接返回异常消息。
     *
     * @param e 异常。
     * @return 异常消息。
     */
    public static String findMessage(Exception e) {
        if (e instanceof InitializationException) {
            e = (Exception) Optional.ofNullable(e.getCause()).orElse(e);
        }
        if (e instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> violationSet = ((ConstraintViolationException) e).getConstraintViolations();
            if (!ObjectUtils.isEmpty(violationSet)) {
                return violationSet.stream()
                        .map(Validations::formatMessage)
                        .findFirst()
                        .orElse(Exceptions.ofMessage(e));
            }
        }
        return Exceptions.ofMessage(e);
    }

    private static String formatMessage(ConstraintViolation<?> violation) {
        return Strings.lenientFormat("[%s] - %s", violation.getPropertyPath(), violation.getMessage());
    }
}
