package com.github.mrzhqiang.rowing.init;

import org.springframework.util.ClassUtils;

/**
 * 初始化器。
 * <p>
 * 主要是进行一些初始化工作，这个接口定义了一些规则，以便更好的记录并执行初始化。
 */
public interface Initializer extends Runnable {

    /**
     * 路径。
     * <p>
     * 通过实现类的全限定类名可以保证全局唯一。
     *
     * @return 初始化器的路径。
     */
    default String getPath() {
        return ClassUtils.getQualifiedName(getClass());
    }

    /**
     * 是否自动运行。
     * <p>
     * 自动运行的初始化器，将在系统启动时，自动调用运行方法。
     *
     * @return 返回 true 表示自动运行；否则表示手动运行。
     */
    default boolean isAutoRun() {
        return false;
    }

    /**
     * 是否每次运行。
     * <p>
     * 自动运行的任务，通常情况下只运行一次，如果开启每次运行，则每次启动都将自动运行。
     *
     * @return 返回 true 表示每次运行；否则表示只运行一次。
     */
    default boolean isEachRun() {
        return true;
    }
}
