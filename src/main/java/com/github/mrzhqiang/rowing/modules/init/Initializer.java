package com.github.mrzhqiang.rowing.modules.init;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

/**
 * 初始化器。
 * <p>
 * 注意：实现类必须标记为 {@link Component Spring 组件}，否则无法被记录到数据库，并且无法自动执行。
 * <p>
 * 另外，应该在 {@link InitializationOrderRegistration} 中对实现类进行顺序注册，否则无法保证执行顺序。
 * <p>
 * 关于 {@link Ordered} 接口：用于对初始化顺序进行排序。
 */
public interface Initializer extends Ordered {

    /**
     * 执行初始化。
     * <p>
     * 此方法上的事务，只有遇到初始化异常才会回滚。
     * <p>
     * 如果当前存在事务，那么会开启一个新事务，所以不会回滚当前事务。
     */
    @Transactional(rollbackFor = InitializationException.class, propagation = Propagation.REQUIRES_NEW)
    void execute();

    /**
     * 此初始化器的唯一路径。
     * <p>
     * 必须保证全局唯一，通过实现类的全限定类名可以保证全局唯一。
     *
     * @return 初始化器的路径。不会返回 Null 值。
     */
    default String getPath() {
        return ClassUtils.getQualifiedName(getClass());
    }

    /**
     * 是否自动执行。
     * <p>
     * 自动执行的初始化器，将在系统启动时，自动调用执行方法。
     *
     * @return 返回 true 表示自动执行；否则表示手动执行。
     */
    default boolean isAutoExecute() {
        return false;
    }

    /**
     * 是否支持重复。
     * <p>
     * 支持重复的初始化器，通常在执行完成后，自动设置为默认状态，以便下次执行。
     *
     * @return 返回 true 表示重复；否则表示不重复。
     */
    default boolean isSupportRepeat() {
        return true;
    }

    @Override
    default int getOrder() {
        // 从注册登记中获得执行顺序，有执行要求的初始化器必须注册登记
        return InitializationOrderRegistration.find(this);
    }
}
