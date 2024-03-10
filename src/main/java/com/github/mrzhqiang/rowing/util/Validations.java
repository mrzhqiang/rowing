package com.github.mrzhqiang.rowing.util;

import com.github.mrzhqiang.helper.Exceptions;
import com.github.mrzhqiang.rowing.init.InitializationException;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import org.hibernate.exception.DataException;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 验证工具。
 */
@UtilityClass
public class Validations {

    private static final List<Class<?>> IGNORE_EXCEPTION_LIST = ImmutableList.of(
            InitializationException.class,
            DataIntegrityViolationException.class,
            DataException.class,
            GenericJDBCException.class,
            JpaSystemException.class,
            TransactionSystemException.class
    );

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
    public static String findMessage(Throwable e) {
        Exception exception = (Exception) findRealCause(e);
        if (exception instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> violationSet = ((ConstraintViolationException) e).getConstraintViolations();
            if (!ObjectUtils.isEmpty(violationSet)) {
                return violationSet.stream()
                        .map(Validations::formatMessage)
                        .findFirst()
                        .orElse(Exceptions.ofMessage(exception));
            }
        }
        return Exceptions.ofMessage(exception);
    }

    private static Throwable findRealCause(Throwable e) {
        if (e == null) {
            return null;
        }
        if (IGNORE_EXCEPTION_LIST.contains(e.getClass())) {
            e = findRealCause(e.getCause());
        }
        return e;
    }

    private static String formatMessage(ConstraintViolation<?> violation) {
        return Strings.lenientFormat("[%s] - %s", violation.getPropertyPath(), violation.getMessage());
    }

}
