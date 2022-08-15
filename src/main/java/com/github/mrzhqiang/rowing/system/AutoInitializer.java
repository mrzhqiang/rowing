package com.github.mrzhqiang.rowing.system;

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
     * 初始化名称。
     * <p>
     * 实现类必须保证此名称的全局唯一性。
     */
    @Nonnull
    String getName();

    /**
     * 执行初始化。
     *
     * @throws Exception 任何异常。允许抛出任何异常，表示由调用方捕捉异常并进行相应处理。
     */
    void execute() throws Exception;
}
