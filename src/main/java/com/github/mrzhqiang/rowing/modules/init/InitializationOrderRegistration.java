package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.modules.account.AccountInitializer;
import com.github.mrzhqiang.rowing.modules.dict.DataDictInitializer;
import com.github.mrzhqiang.rowing.modules.menu.MenuInitializer;
import com.github.mrzhqiang.rowing.modules.setting.SysSettingInitializer;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 初始化执行顺序登记。
 *
 * @see org.springframework.security.config.annotation.web.builders.FilterOrderRegistration
 */
@SuppressWarnings("JavadocReference")
final class InitializationOrderRegistration {

    /**
     * 起始顺序。
     */
    private static final int INITIAL_ORDER = 100;
    /**
     * 排序步长。
     * <p>
     * 这里参考的是 Spring Security 的 Filter 排序，说明至少可以新增 99 个同类型步骤。
     */
    private static final int ORDER_STEP = 100;
    /**
     * 初始化顺序映射。
     */
    private static final Map<String, Integer> INITIALIZER_TO_ORDER = new HashMap<>();

    static {
        OrderStep order = new OrderStep(INITIAL_ORDER, ORDER_STEP);
        // 多语言初始化
        // 字典初始化
        put(DataDictInitializer.class, order.next());
        // 设置初始化
        put(SysSettingInitializer.class, order.next());
        // 菜单初始化
        put(MenuInitializer.class, order.next());
        // 账户初始化
        put(AccountInitializer.class, order.next());
    }

    /**
     * Register a {@link Initializer} with its specific position. If the {@link Initializer} was
     * already registered before, the position previously defined is not going to be overridden
     *
     * @param initializerClass the {@link Initializer} to register
     * @param position         the position to associate with the {@link Filter}
     */
    private static void put(Class<? extends Initializer> initializerClass, int position) {
        String className = initializerClass.getName();
        if (INITIALIZER_TO_ORDER.containsKey(className)) {
            return;
        }
        INITIALIZER_TO_ORDER.put(className, position);
    }

    /**
     * Gets the order of a particular {@link Initializer} class taking into consideration
     * superclasses.
     *
     * @param clazz the {@link Initializer} class to determine the sort order
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
     * 只有运行时才需要知道初始化顺序，因此我们不使用 {@link #getOrder(Class)} 方法，而是使用对象实例。
     *
     * @param obj 初始化实现类的实例。
     * @return 初始化顺序。默认情况下返回 {@link #INITIAL_ORDER 初始顺序}。
     */
    static Integer find(Object obj) {
        if (obj == null) {
            return INITIAL_ORDER;
        }

        Integer order = INITIALIZER_TO_ORDER.get(obj.getClass().getName());
        return Optional.ofNullable(order).orElse(INITIAL_ORDER);
    }
}
