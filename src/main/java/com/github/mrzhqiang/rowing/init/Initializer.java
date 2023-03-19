package com.github.mrzhqiang.rowing.init;

import org.springframework.util.ClassUtils;

/**
 * 初始化器。
 * <p>
 * 主要是进行一些初始化工作，这个接口定义了一些规则，以便更好的记录并执行初始化。
 */
public interface Initializer {

    /**
     * 执行初始化。
     * <p>
     * 注意，执行初始化应该在新的事务下进行，当遇到异常时，可以回滚执行的操作，且不影响调用方。
     */
    void execute();

    /**
     * 路径。
     * <p>
     * 通过实现类的全限定类名可以保证全局唯一。
     *
     * @return 初始化器的路径。
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

}
