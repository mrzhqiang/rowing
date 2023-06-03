package com.github.mrzhqiang.rowing.domain;

/**
 * 任务模式。
 * <p>
 * 表示任务的执行模式，通常任务只执行一次，如果需要反复执行，那么就要设定任务模式。
 */
public enum TaskMode {

    /**
     * 仅一次。
     * <p>
     * 表示任务仅执行一次，后续不再重复执行。
     */
    ONCE,
    /**
     * 每一次。
     * <p>
     * 表示任务每次都执行。
     */
    EACH,
}
