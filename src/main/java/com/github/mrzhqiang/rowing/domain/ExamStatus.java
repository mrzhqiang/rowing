package com.github.mrzhqiang.rowing.domain;

/**
 * 考试状态。
 * <p>
 */
public enum ExamStatus {

    /**
     * 默认。
     */
    DEFAULT,
    /**
     * 待开始。
     */
    WAITING,
    /**
     * 考试中。
     */
    RUNNING,
    /**
     * 阅卷中。
     */
    MARKING,
    /**
     * 已完成。
     */
    FINISHED,

}
