package com.github.mrzhqiang.rowing.exception;

import com.github.mrzhqiang.helper.Exceptions;
import com.github.mrzhqiang.rowing.init.InitializationException;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.experimental.UtilityClass;
import org.hibernate.exception.DataException;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.ObjectUtils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 异常原因工具。
 */
@UtilityClass
public class ExceptionCauses {

    private static final List<Class<?>> IGNORE_EXCEPTION_LIST = ImmutableList.of(
            InitializationException.class,
            DataIntegrityViolationException.class,
            DataException.class,
            GenericJDBCException.class,
            JpaSystemException.class,
            TransactionSystemException.class
    );

    /**
     * 从异常中找到消息。
     * <p>
     * 注意：如果是验证框架相关的异常，则获取验证失败的第一条消息返回；否则直接返回异常消息。
     *
     * @param throwable 异常。
     * @return 异常消息。
     */
    public static String findMessage(Throwable throwable) {
        Exception exception = (Exception) findRealCause(throwable);
        if (exception == null) {
            return Exceptions.UNKNOWN_MESSAGE;
        }

        if (exception instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> violationSet = ((ConstraintViolationException) throwable).getConstraintViolations();
            if (!ObjectUtils.isEmpty(violationSet)) {
                return violationSet.stream()
                        .map(ExceptionCauses::formatMessage)
                        .findFirst()
                        .orElseGet(() -> Exceptions.ofMessage(exception));
            }
        }

        return Exceptions.ofMessage(exception);
    }

    private static Throwable findRealCause(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        if (IGNORE_EXCEPTION_LIST.contains(throwable.getClass())) {
            throwable = findRealCause(throwable.getCause());
        }
        return throwable;
    }

    private static String formatMessage(ConstraintViolation<?> violation) {
        return Strings.lenientFormat("[%s] - %s", violation.getPropertyPath(), violation.getMessage());
    }

}
