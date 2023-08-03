package com.github.mrzhqiang.rowing.util;

import com.github.mrzhqiang.helper.Exceptions;
import com.google.common.base.Strings;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 验证工具。
 */
public final class Validations {
    private Validations() {
        // no instances.
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
