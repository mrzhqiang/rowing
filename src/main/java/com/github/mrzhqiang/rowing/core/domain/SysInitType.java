package com.github.mrzhqiang.rowing.core.domain;

/**
 * 系统初始化类型。
 */
public enum SysInitType {

    /**
     * 必选类型。
     * <p>
     * 表示此类初始化操作，在系统启动时自动执行。
     */
    REQUIRED,
    /**
     * 可选类型。
     * <p>
     * 表示此类初始化操作，由管理员决定是否执行，
     */
    OPTIONAL,
}
