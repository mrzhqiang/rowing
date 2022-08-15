package com.github.mrzhqiang.rowing.domain;

import lombok.Getter;

/**
 * 任务状态。
 */
@Getter
public enum TaskStatus {
    /**
     * 默认状态。
     * <p>
     * 表示任务未开始。
     */
    DEFAULT("默认"),
    /**
     * 启动状态。
     * <p>
     * 表示任务运行中。
     */
    STARTED("运行中"),
    /**
     * 失败状态。
     * <p>
     * 表示任务出现问题导致失败。
     */
    FAILED("失败"),
    /**
     * 完成状态。
     * <p>
     * 表示任务执行成功。
     */
    COMPLETED("成功"),;

    final String label;


    TaskStatus(String label) {
        this.label = label;
    }
}
