package com.github.mrzhqiang.rowing.system.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Action {

    /**
     * 操作名称。
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
