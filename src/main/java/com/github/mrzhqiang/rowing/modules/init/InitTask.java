package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.domain.TaskType;
import com.github.mrzhqiang.rowing.modules.i18n.Language;
import com.github.mrzhqiang.rowing.util.Exceptions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.util.Objects;

/**
 * 初始化任务。
 * <p>
 * 保证系统开箱即用，在系统异常时也可以用于恢复出厂设置。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class InitTask extends AuditableEntity {

    /**
     * 名称。
     * <p>
     * 通常是实现类的类名称，如果国际化消息不存在，则作为保底文本显示。
     */
    @Column(nullable = false)
    private String name;
    /**
     * 国际化。
     * <p>
     * 通常是国际化语言的 key 名称，通过它可以获取当前用户语言对应的国际化名称。
     */
    @ManyToOne
    private Language i18n;
    /**
     * 路径。
     * <p>
     * 实际上是实现类的全限定类名称，用于确定全局唯一性。
     * <p>
     * 通过路径可以获取对应实体数据，确保已完成的系统任务不会重复执行。
     * <p>
     * 同时也可以作为实例化参数，在需要时创建对应的初始化任务实例。
     */
    @Column(unique = true, nullable = false, updatable = false)
    private String path;
    /**
     * 类型。
     * <p>
     * 定义在不同的场景下执行的任务类型。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TaskType type;
    /**
     * 是否废弃。
     * <p>
     * 如果删除不再使用的枚举类代码，但忘记清理相关数据的话，将在同步数据时标记为已废弃。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Logic discard;
    /**
     * 状态。
     * <p>
     * 新的初始化任务总是默认状态，启动后则成为已启动状态，随后因为运行成功或失败，而变成已完成或已失败状态。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
    /**
     * 开始时间。
     * <p>
     * 当任务开始时，记录开始时间，方便展示最近一次执行的时间戳。
     */
    private Instant startTime;
    /**
     * 结束时间。
     * <p>
     * 当任务成功或失败时，记录结束时间，方便展示最近一次成功的时间戳。
     */
    private Instant endTime;
    /**
     * 错误消息。
     * <p>
     * 当任务失败时，记录当前异常消息；如果是其他状态，则记录的是上次异常消息。
     */
    private String errorMessage;
    /**
     * 错误跟踪。
     * <p>
     * 当任务失败时，记录当前异常堆栈；如果是其他状态，则记录的是上次异常堆栈。
     */
    @Column(length = 2000)
    private String errorTrace;
    /**
     * 执行顺序。
     * <p>
     * 为初始化任务定义执行顺序，避免后续任务缺少相关依赖。
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Initializer) {
            return Objects.equals(path, ((Initializer) o).getPath());
        }
        return super.equals(o);
    }
}
