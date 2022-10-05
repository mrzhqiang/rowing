package com.github.mrzhqiang.rowing.module.system.init;

import com.github.mrzhqiang.rowing.module.domain.BaseEntity;
import com.github.mrzhqiang.rowing.module.domain.SysInitType;
import com.github.mrzhqiang.rowing.module.domain.TaskStatus;
import com.github.mrzhqiang.rowing.util.Exceptions;
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
 * 这样可以有效控制初始化任务，在执行成功之后，不再重复执行。
 * <p>
 * 如果需要重新执行，也可以通过界面或者数据库，直接删除对应初始化任务数据，从而在下一次系统启动时自动执行。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class SysInit extends BaseEntity {

    /**
     * 初始化类型。
     * <p>
     * 主要定义自动执行（必选）还是手动执行（可选）。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SysInitType type;
    /**
     * 初始化名称。
     * <p>
     * 用于界面展示，以便屏蔽代码细节。
     */
    @Column(nullable = false)
    private String name;
    /**
     * 初始化路径。
     * <p>
     * 实际上是执行类的名称，可以确定全局唯一性，避免重复执行初始化任务。
     */
    @Column(unique = true, nullable = false)
    private String path;
    /**
     * 初始化任务状态。
     * <p>
     * 新的初始化任务总是默认状态，启动后则成为已启动状态，随后因为运行成功或失败，而变成已完成或已失败状态。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
    /**
     * 开始时间。
     * <p>
     * 只要状态成为已启动，则开始时间要记录当前时间戳，同时清空结束时间，避免时间段交叉问题。
     */
    private Instant startTime;
    /**
     * 结束时间。
     * <p>
     * 只要状态为已完成或已失败，则结束时间要记录当前时间戳。
     */
    private Instant endTime;
    /**
     * 错误消息。
     * <p>
     * 如果是已失败状态，则记录的是当前异常消息；如果是已启动或已完成状态，则记录的是上次异常消息。
     */
    private String errorMessage;
    /**
     * 错误跟踪。
     * <p>
     * 如果是已失败状态，则记录的是当前异常堆栈；如果是已启动或已完成状态，则记录的是上次异常堆栈。
     */
    @Column(length = 2000)
    private String errorTrace;
    /**
     * 初始化执行顺序。
     * <p>
     * 按顺序执行的初始化任务，可以避免潜在的依赖问题。
     */
    @Column(nullable = false)
    private Integer ordered;

    /**
     * 设为已启动状态。
     */
    public void withStarted() {
        status = TaskStatus.STARTED;
        startTime = Instant.now();
        endTime = null;
    }

    /**
     * 设为已失败状态。
     *
     * @param error 错误异常。
     */
    public void withFailed(Exception error) {
        status = TaskStatus.FAILED;
        endTime = Instant.now();
        errorMessage = Exceptions.ofMessage(error);
        errorTrace = Exceptions.ofTrace(error);
    }

    /**
     * 设为已完成状态。
     */
    public void withCompleted() {
        status = TaskStatus.COMPLETED;
        endTime = Instant.now();
    }

    /**
     * 判断是否未完成。
     * <p>
     * 只要不是已完成，就都是未完成状态，包括已失败状态。
     *
     * @return 返回 true 表示未完成；否则表示已完成。
     */
    public boolean isNotCompleted() {
        return !TaskStatus.COMPLETED.equals(status);
    }
}
