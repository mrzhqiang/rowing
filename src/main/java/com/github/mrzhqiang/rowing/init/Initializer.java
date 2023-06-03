package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.TaskMode;
import com.github.mrzhqiang.rowing.domain.TaskType;
import org.springframework.util.ClassUtils;

/**
 * 初始化器。
 * <p>
 * 初始化指的是，在系统启动时，执行一些初始任务，比如同步字典（来自枚举、Excel、Sql等）到数据库，预热缓存（字典数据、热点数据）。
 * <p>
 * 为了保证每个初始化器都独一无二，可以使用类的全限定名称作为路径。
 * <p>
 * 通常随系统启动而自动执行的初始任务，称之为系统任务，除此之外的任务为可选任务，即支持从管理后台手动执行。
 * <p>
 * 有时候，初始任务只在第一次系统启动时执行，某些特殊的初始化可能需要每一次系统启动时执行。
 * <p>
 * 为了支持多线程，这个接口继承了 {@link Runnable} 接口。
 */
public interface Initializer extends Runnable {

    /**
     * 路径。
     * <p>
     * 通过实现类的全限定类名可以保证全局唯一。
     * <p>
     * 不建议重写此方法。
     *
     * @return 初始化器的路径。
     */
    default String getPath() {
        return ClassUtils.getQualifiedName(getClass());
    }

    /**
     * 类型。
     * <p>
     * 默认情况下是可选的初始化器。
     * <p>
     * 可以重写此方法，返回不同的任务类型。
     *
     * @return 任务类型。
     */
    default TaskType getType() {
        return TaskType.OPTIONAL;
    }

    /**
     * 模式。
     * <p>
     * 默认情况下是仅一次的初始化器。
     * <p>
     * 可以重写此方法，返回不同的任务模式。
     *
     * @return 任务模式。
     */
    default TaskMode getMode() {
        return TaskMode.ONCE;
    }
}
