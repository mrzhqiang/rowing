package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Enums;

/**
 * 任务状态。
 * <p>
 * 任务具有一定的状态，表示不同的执行阶段。
 */
public enum TaskStatus {

    /**
     * 默认状态。
     * <p>
     * 表示任务未执行。
     */
    DEFAULT,
    /**
     * 启动状态。
     * <p>
     * 表示任务正在运行中。
     */
    STARTED,
    /**
     * 失败状态。
     * <p>
     * 表示任务出现问题导致失败。
     */
    FAILED,
    /**
     * 完成状态。
     * <p>
     * 表示任务执行成功。
     */
    COMPLETED,
    ;

    public static TaskStatus of(String status) {
        return Enums.findByNameIgnoreCase(TaskStatus.class, status, DEFAULT);
    }

}
