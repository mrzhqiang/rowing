package com.github.mrzhqiang.rowing.api.domain;

/**
 * 任务状态。
 * <p>
 * 简单的任务一般只有两种状态：默认状态、已完成状态。
 * <p>
 * 不存在中间状态，也就是说，即使失败也不会保存状态。
 */
public enum TaskStatus {
    /**
     * 默认状态。
     * <p>
     * 表示任务未完成，这有可能是未执行过、正在执行中以及执行出错，我们统一视为默认状态。
     */
    DEFAULT,
    /**
     * 已完成状态。
     */
    FINISHED,
}
