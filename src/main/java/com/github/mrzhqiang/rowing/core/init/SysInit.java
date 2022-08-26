package com.github.mrzhqiang.rowing.core.init;

import com.github.mrzhqiang.rowing.core.domain.BaseEntity;
import com.github.mrzhqiang.rowing.core.domain.SysInitType;
import com.github.mrzhqiang.rowing.core.domain.TaskStatus;
import com.github.mrzhqiang.rowing.util.Exceptions;
import com.google.common.base.Throwables;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;

/**
 * 系统初始化。
 * <p>
 * 主要用于记录系统初始化任务的执行情况。
 * <p>
 * 这样可以控制每个初始化任务，在执行成功之后，不再重复执行。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class SysInit extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SysInitType type;
    @Column(unique = true, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
    private Instant startTime;
    private Instant endTime;
    private String errorMessage;
    @Column(length = 2000)
    private String errorTrace;
    @Column(nullable = false)
    private Integer ordered;

    public void withStarted() {
        status = TaskStatus.STARTED;
        startTime = Instant.now();
    }

    public void withFailed(Exception error) {
        status = TaskStatus.FAILED;
        errorMessage = Exceptions.ofMessage(error);
        errorTrace = Exceptions.ofTrace(error);
    }

    public void withCompleted() {
        status = TaskStatus.COMPLETED;
        endTime = Instant.now();
    }

    public boolean isCompleted() {
        return TaskStatus.COMPLETED.equals(status);
    }
}
