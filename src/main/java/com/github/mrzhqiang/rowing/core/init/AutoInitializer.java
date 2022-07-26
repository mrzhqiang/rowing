package com.github.mrzhqiang.rowing.core.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 自动初始化器。
 * <p>
 * 注意：必须将此接口的实现类标记为 {@link Component} 组件。并在 {@link AutoInitializationOrderRegistration} 中进行顺序注册，否则将无法保证初始化顺序。
 * <p>
 * 关于 CommandLineRunner 接口：用于在系统启动时，自动调用当前接口的实现类。
 * <p>
 * 关于 Ordered 接口：用于对初始化顺序进行排序。
 */
public interface AutoInitializer extends CommandLineRunner, Ordered {

    /**
     * 判断是否已经初始化。
     *
     * @return 返回 True 表示已经初始化；返回 False 则自动开始初始化操作。
     */
    default boolean hasInitialized() {
        return false;
    }

    /**
     * 自动初始化器的名称。
     * <p>
     * 实际上返回的是实现类的全限定类名，因此不要随意修改扩展类的包名，会导致旧数据不可用。
     *
     * @return 实际上是 XXX.class.getName() 返回的字符串。
     */
    default String getName() {
        return this.getClass().getName();
    }

    /**
     * 执行初始化的方法。
     * <p>
     * 通过实现它来真正执行初始化，在 hasInitialized 返回 false 时自动调用；返回 true 时自动跳过。
     *
     * @return True 表示初始化完成；Fasle 表示初始化失败。
     * @throws Exception 初始化过程可能抛出的异常。
     */
    boolean onInit() throws Exception;

    /**
     * 初始化完成后的回调方法。
     */
    void onFinished();

    @Override
    default void run(String... args) throws Exception {
        if (hasInitialized()) {
            return;
        }

        if (onInit()) {
            onFinished();
        }
    }

    @Override
    default int getOrder() {
        return AutoInitializationOrderRegistration.find(this);
    }
}
