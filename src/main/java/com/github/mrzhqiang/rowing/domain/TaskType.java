package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Enums;

/**
 * 任务类型。
 * <p>
 * 通常的任务类型分为系统任务、可选任务。
 */
public enum TaskType {

    /**
     * 系统任务。
     * <p>
     * 表示在系统启动时自动执行，也支持手动执行。
     */
    SYSTEM,
    /**
     * 可选任务。
     * <p>
     * 表示需要通过手动执行，
     */
    OPTIONAL,
    ;

    public static TaskType of(String type) {
        return Enums.findByNameIgnoreCase(TaskType.class, type, SYSTEM);
    }

}
