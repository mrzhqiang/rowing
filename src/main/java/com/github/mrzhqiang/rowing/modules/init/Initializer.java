package com.github.mrzhqiang.rowing.modules.init;

import org.springframework.util.ClassUtils;

/**
 * 初始化器。
 */
public interface Initializer {

    /**
     * 执行初始化。
     * <p>
     * 此方法用于执行一段初始化逻辑。
     */
    void execute();

    /**
     * 获取路径。
     * <p>
     * 必须保证全局唯一，通过实现类的全限定类名可以保证全局唯一。
     *
     * @return 初始化器的路径。不会返回 Null 值。
     */
    default String getPath() {
        return ClassUtils.getQualifiedName(getClass());
    }

    /**
     * 获取名称。
     * <p>
     * 名称用于展示，默认是实现类名称。
     *
     * @return 初始化器的名称。不会返回 Null 值。
     */
    default String getName() {
        return ClassUtils.getShortName(getClass());
    }
}
