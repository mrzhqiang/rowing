package com.github.mrzhqiang.rowing.system.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * 自动初始化器。
 * <p>
 * 注意：必须将此接口的实现类标记为 {@link Component} 组件，否则将无法被记录到数据库，并且无法自动执行。
 * <p>
 * 另外，应该在 {@link AutoInitializationOrderRegistration} 中进行顺序注册，否则将无法保证执行顺序。
 * <p>
 * 关于 {@link CommandLineRunner} 接口：用于在系统启动时，自动调用当前接口的实现类。
 * <p>
 * 关于 {@link Ordered} 接口：用于对初始化顺序进行排序。
 */
public interface AutoInitializer extends CommandLineRunner, Ordered {

    /**
     * 初始化名称。
     * <p>
     * 用来在界面展示初始化任务名称，通常是类名称，后续可以通过 i18n 模块显示系统当前语言的名称。
     */
    @Nonnull
    String getName();

    /**
     * 初始化路径。
     * <p>
     * 实现类必须保证此路径的全局唯一性。
     */
    @Nonnull
    String getPath();

    /**
     * 执行初始化。
     *
     * @throws Exception 任何异常。允许抛出任何异常，表示由调用方捕捉异常并进行相应处理。
     */
    void execute() throws Exception;

}
