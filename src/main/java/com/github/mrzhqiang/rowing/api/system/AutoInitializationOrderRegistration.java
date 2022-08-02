package com.github.mrzhqiang.rowing.api.system;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 自动初始化的执行顺序登记。
 *
 * @see org.springframework.security.config.annotation.web.builders.FilterOrderRegistration
 */
@SuppressWarnings("JavadocReference")
final class AutoInitializationOrderRegistration {

    /**
     * 起始顺序。
     */
    private static final int INITIAL_ORDER = 100;
    /**
     * 排序步长。
     * <p>
     * 这里参考的是 Spring Security 的 Filter 排序。
     */
    private static final int ORDER_STEP = 100;

    private static final Map<String, Integer> INITIALIZER_TO_ORDER = new HashMap<>();

    static {
        OrderStep order = new OrderStep(INITIAL_ORDER, ORDER_STEP);
        put(DataDictAutoInitializer.class, order.next());
        put(SysSettingAutoInitializer.class, order.next());
    }

    /**
     * Register a {@link AutoInitializer} with its specific position. If the {@link AutoInitializer} was
     * already registered before, the position previously defined is not going to be overridden
     *
     * @param initializerClass the {@link AutoInitializer} to register
     * @param position         the position to associate with the {@link Filter}
     */
    private static void put(Class<? extends AutoInitializer> initializerClass, int position) {
        String className = initializerClass.getName();
        if (INITIALIZER_TO_ORDER.containsKey(className)) {
            return;
        }
        INITIALIZER_TO_ORDER.put(className, position);
    }

    /**
     * Gets the order of a particular {@link AutoInitializer} class taking into consideration
     * superclasses.
     *
     * @param clazz the {@link AutoInitializer} class to determine the sort order
     * @return the sort order or null if not defined
     */
    @SuppressWarnings("unused")
    static Integer getOrder(Class<?> clazz) {
        while (clazz != null) {
            Integer result = INITIALIZER_TO_ORDER.get(clazz.getName());
            if (result != null) {
                return result;
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * 找到顺序。
     * <p>
     * 不使用 {@link #getOrder(Class)} 方法，因为自动初始化接口的扩展类继承自 {@link BaseAutoInitializer} 且一般不会再扩展子类。
     */
    static Integer find(Object obj) {
        if (obj == null) {
            return INITIAL_ORDER;
        }

        Integer order = INITIALIZER_TO_ORDER.get(obj.getClass().getName());
        return Optional.ofNullable(order).orElse(INITIAL_ORDER);
    }
}
