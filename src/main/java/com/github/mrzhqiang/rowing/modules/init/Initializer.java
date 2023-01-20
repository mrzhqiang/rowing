package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.exception.InitializeException;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.Nonnull;

/**
 * 初始化器。
 * <p>
 * 注意：必须将此接口的实现类标记为 {@link Component} 组件，否则将无法被记录到数据库，并且执行时无法找到对应实例。
 */
public interface Initializer {

    /**
     * 获取路径。
     * <p>
     * 必须保证全局唯一，因此可以获取实现类的全限定类名。
     *
     * @return 路径。不会返回 Null 值。
     */
    @Nonnull
    default String getPath() {
        return ClassUtils.getQualifiedName(this.getClass());
    }

    /**
     * 执行。
     * <p>
     * 表示执行初始化逻辑的方法，由具体类实现。
     *
     * @throws InitializeException 初始化异常。表示调用方必须捕捉此类异常并进行相应处理。
     */
    void execute() throws InitializeException;
}
