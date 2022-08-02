package com.github.mrzhqiang.rowing.api.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * 自动初始化器。
 * <p>
 * 注意：必须将此接口的实现类标记为 {@link Component} 组件。并在 {@link AutoInitializationOrderRegistration} 中进行顺序注册，否则将无法保证初始化及其顺序。
 * <p>
 * 关于 CommandLineRunner 接口：用于在系统启动时，自动调用当前接口的实现类。
 * <p>
 * 关于 Ordered 接口：用于对初始化顺序进行排序。
 */
public interface AutoInitializer extends CommandLineRunner, Ordered {

    /**
     * 自动初始化器的名称。
     *
     * @return 返回自动初始化器的名称，需保证全局唯一性，不能返回 Null 值。
     */
    @Nonnull
    String getName();

    /**
     * 判断是否已经初始化。
     *
     * @return 返回 True 表示已经初始化；返回 False 则自动开始初始化操作。
     */
    boolean hasInitialized();

    /**
     * 尝试进行初始化操作。
     * <p>
     * 注意：一般这个方法调用 service 进行处理，由 service 管理事务。
     */
    void attemptInitialize();
}
