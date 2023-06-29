package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.aop.SystemAuth;

/**
 * 系统认证范围。
 * <p>
 * 用于确定 {@link SystemAuth} 的生效范围。
 *
 * @see SystemAuth
 */
public enum SystemAuthScope {

    /**
     * 默认。
     * <p>
     * 表示在无用户认证的情况下，默认设置为系统认证，当方法调用完成，再回退到无用户认证。
     * <p>
     * 如果已有用户认证，则不进行任何替换操作。
     */
    DEFAULT,
    /**
     * 全局。
     * <p>
     * 表示在全局范围内生效。这通常会覆盖已有认证，即当前登录用户会丢失会话，导致下次请求需要重新登录认证。
     */
    GLOBAL,
    /**
     * 当前。
     * <p>
     * 表示在当前范围内生效。这意味着只在当前方法上替换已有认证，当方法执行完毕后，将还原到之前的认证。
     */
    CURRENT,
}
