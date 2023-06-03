package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.account.SystemUserMock;

/**
 * 模拟范围。
 * <p>
 * 用于确定 {@link SystemUserMock} 的生效范围。
 *
 * @see SystemUserMock
 */
public enum MockScope {

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
