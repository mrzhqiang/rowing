package com.github.mrzhqiang.rowing.action;

import com.github.mrzhqiang.rowing.domain.ActionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作注解。
 * <p>
 * 用于标记方法，记录操作日志。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Action {

    /**
     * 操作值。
     *
     * @return 操作类型。使用枚举字典的国际化名称，作为操作名称。
     */
    ActionType value() default ActionType.NONE;

}
