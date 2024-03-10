package com.github.mrzhqiang.rowing.account;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作为系统用户运行。
 * <p>
 * 被此注解标记的方法，将作为系统用户运行，直到方法运行完成，再还原为之前的认证信息。
 *
 * @see RunAsSystemAspect
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RunAsSystem {
}
