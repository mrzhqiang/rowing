package com.github.mrzhqiang.rowing.domain;

/**
 * 任务类型。
 * <p>
 * 通常的任务类型分为系统任务、可选任务。
 */
public enum TaskType {

    /**
     * 系统任务。
     * <p>
     * 表示此类任务，在系统启动时自动执行。
     */
    SYSTEM,
    /**
     * 可选任务。
     * <p>
     * 表示此类任务，通过管理后台手动执行，
     */
    OPTIONAL,
}
