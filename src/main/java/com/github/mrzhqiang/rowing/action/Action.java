package com.github.mrzhqiang.rowing.action;

import com.github.mrzhqiang.rowing.domain.ActionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作。
 * <p>
 * 特别注意：这个注解只能在 web 请求相关的方法上执行，如果是由内部调用的方法，将因为找不到对应会话而抛出异常。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Action {

    /**
     * 操作名称。
     * <p>
     * 建议最大不超过 50 个字符。
     *
     * @return 获取操作名称，用来记录到 ActionLog 实体的 action 字段。
     */
    String value() default "";

    /**
     * 操作类型。
     *
     * @return 获取操作类型，用来记录到 ActionLog 实体的 type 字段。
     */
    ActionType type() default ActionType.NONE;

}
