package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.MockScope;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记作为系统用户。
 * <p>
 * 用于标记目标方法，在目标方法被执行时，将自动替换认证信息为系统用户，执行完毕后，根据对应范围决定是还原还是清理已有的认证信息。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SystemUserMock {
    /**
     * 范围。
     * <p>
     * 默认情况下，属于全局范围，即在当前线程上进行系统用户认证，执行完方法后进行清理。
     *
     * @return 操作人范围。
     */
    MockScope scope() default MockScope.GLOBAL;
}
