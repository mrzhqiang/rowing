package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.TaskType;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自动初始化器。
 * <p>
 * 支持在系统启动时自动执行。
 * <p>
 * 需要在 {@link InitializationOrderRegistration} 中对实现类进行顺序注册，否则无法保证执行顺序。
 * <p>
 * 注意：实现类必须标记为 {@link Component}，否则无法被记录到数据库，并且无法自动执行。
 * <p>
 * 关于 {@link Ordered} 接口：用于对自动初始化器的执行顺序进行排序。
 *
 * @see InitializationOrderRegistration
 */
public abstract class AutoInitializer implements Initializer, Ordered {

    /**
     * 开始回调。
     * <p>
     * 在执行自动初始化器之前回调此方法。
     * <p>
     * 注意：这个方法如果抛出异常，将中断当前初始化，不会调用 {@link #onExecute()} 方法。
     * <p>
     * 只有抛出 {@link InitializationException} 异常才会触发事务回滚。
     */
    protected void onStart() {
        // do nothing
    }

    /**
     * 执行回调。
     * <p>
     * 这里是扩展类需要实现的方法，专注业务逻辑，无需关心相关状态。
     *
     * @throws Exception 允许抛出的任何异常。通常会转为 {@link InitializationException} 异常，确保事务回滚。
     */
    protected abstract void onExecute() throws Exception;

    /**
     * 错误回调。
     * <p>
     * 在执行自动初始化器时，如果捕捉到任何异常，将回调此方法。
     * <p>
     * 通常此方法与 {@link #onComplete()} 方法互斥。
     * 也就是说，在一次调用过程中，要么回调完成方法，要么回调错误方法，不会同时回调。
     * <p>
     * 注意：这个方法如果抛出异常，将中止当前初始化，不会影响执行回调。
     * <p>
     * 但如果是抛出 {@link InitializationException} 异常，则会触发事务回滚。
     *
     * @param error 执行方法时的原始异常。
     */
    protected void onError(@SuppressWarnings("unused") Throwable error) {
        // do nothing
    }

    /**
     * 完成回调。
     * <p>
     * 在执行自动初始化器之后，如果没有捕捉到任何异常，将回调此方法。
     * <p>
     * 通常此方法与 {@link #onError(Throwable)} 方法互斥。
     * 也就是说，在一次调用过程中，要么回调完成方法，要么回调错误方法，不会同时回调。
     * <p>
     * 注意：这个方法如果抛出异常，将中止当前初始化，不会影响执行回调。
     * <p>
     * 但如果是抛出 {@link InitializationException} 异常，则会触发事务回滚。
     */
    protected void onComplete() {
        // do nothing
    }

    /**
     * 运行。
     * <p>
     * 关于事务：
     * <p>
     * 1. 此方法每次被调用时，都将创建新的事务，因此自动初始化器的实现类，应保证单一职责原则。
     * <p>
     * 2. 只有遇到 {@link InitializationException} 异常才会回滚。
     */
    @Transactional(rollbackFor = InitializationException.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void run() {
        onStart();
        try {
            onExecute();
        } catch (Exception e) {
            onError(e);
            throw new InitializationException(e);
        }
        onComplete();
    }

    @Override
    public TaskType getType() {
        return TaskType.SYSTEM;
    }

    @Override
    public int getOrder() {
        return InitializationOrderRegistration.find(this);
    }

}
