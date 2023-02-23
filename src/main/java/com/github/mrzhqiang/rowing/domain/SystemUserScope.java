package com.github.mrzhqiang.rowing.domain;

/**
 * 系统用户范围。
 * <p>
 * 用于确定系统用户的影响范围。
 *
 * @see com.github.mrzhqiang.rowing.system.WithSystemUser
 */
public enum SystemUserScope {

    /**
     * 全局。
     * <p>
     * 表示在全局范围内有效，这通常会覆盖已有认证，即当前登录用户会被挤下线。
     */
    GLOBAL,
    /**
     * 当前。
     * <p>
     * 表示在当前范围内有效，这意味着只在当前方法上替换已有认证，当方法执行完毕后，将还原已有认证。
     */
    CURRENT,
}
