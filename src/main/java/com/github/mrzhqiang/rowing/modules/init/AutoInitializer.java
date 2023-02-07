package com.github.mrzhqiang.rowing.modules.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 自动初始化器。
 * <p>
 * 注意：此接口的实现类必须标记为 {@link Component Spring 组件}，否则无法被记录到数据库，并且无法自动执行。
 * <p>
 * 另外，应该在 {@link InitializationOrderRegistration} 中对实现类进行顺序注册，否则无法保证执行顺序。
 * <p>
 * 关于 {@link Initializer} 接口：表示支持执行初始化逻辑。
 * <p>
 * 关于 {@link CommandLineRunner} 接口：用于在系统启动时，自动调用当前接口的实现类。
 * <p>
 * 关于 {@link Ordered} 接口：用于对初始化顺序进行排序。
 */
@Slf4j
public abstract class AutoInitializer extends BaseInitializer
        implements Initializer, CommandLineRunner, Ordered {

    @Override
    public final void run(String... args) {
        // 通过数据库控制是否可执行
        repository.findByPath(getPath())
                .filter(InitTask::isExecutable)
                .ifPresent(this::attemptExecute);
    }

    @Override
    public final int getOrder() {
        // 从注册登记中获得扩展实现的执行顺序，所以有严格要求的自动初始化必须注册登记
        return InitializationOrderRegistration.find(this);
    }
}
