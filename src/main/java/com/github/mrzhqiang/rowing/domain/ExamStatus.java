package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Enums;

/**
 * 考试状态。
 * <p>
 * 新创建的考试是默认状态；
 * 添加了考生和阅卷人之后，如果考试开始时间未到则变成待开始状态，如果考试开始时间到了，则自动变成考试中状态；
 * 如果考试结束时间到了，则自动变成阅卷中状态；
 * 如果阅卷人完成了阅卷，则变成已完成状态；
 * 如果管理员操作暂停考试，则变成考试暂停状态；
 * 如果管理员操作取消考试，则变成考试取消状态；
 * 一旦处于考试暂停状态，则只能由管理员进行恢复，如果当前时间超过结束时间，则无法恢复；
 * 考试取消状态不可恢复，未来可能支持复制考试操作，方便从取消的考试中，复制新的考试。
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
    /**
     * 考试暂停。
     */
    PAUSE,
    /**
     * 考试取消。
     */
    CANCEL,
    ;

    public static ExamStatus of(String status) {
        return Enums.findByNameIgnoreCase(ExamStatus.class, status, DEFAULT);
    }

}
