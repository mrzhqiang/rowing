package com.github.mrzhqiang.rowing.aop;

import com.github.mrzhqiang.rowing.domain.AuthScope;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统用户认证。
 * <p>
 * 用于标记目标方法，在方法执行之前，将自动认证为系统用户，等方法执行完毕，再根据认证范围处理认证信息。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SystemUserAuth {
    /**
     * 认证范围。
     * <p>
     * 默认情况下，属于全局范围，即在当前线程上进行系统用户认证，执行完方法后进行清理。
     *
     * @return 认证范围。
     */
    AuthScope scope() default AuthScope.GLOBAL;
}
