package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.dict.DictAutoInitializer;
import com.github.mrzhqiang.rowing.account.AccountAutoInitializer;
import com.github.mrzhqiang.rowing.menu.MenuAutoInitializer;
import com.github.mrzhqiang.rowing.setting.SettingAutoInitializer;

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

    private static final int INITIAL_ORDER = 100;
    private static final int ORDER_STEP = 100;
    private static final Map<String, Integer> INITIALIZER_TO_ORDER = new HashMap<>();

    static {
        Step order = new Step(INITIAL_ORDER, ORDER_STEP);
        // 以下是具有严格顺序要求的自动初始化实现
        put(DictAutoInitializer.class, order.next());
        put(SettingAutoInitializer.class, order.next());
        put(MenuAutoInitializer.class, order.next());
        put(AccountAutoInitializer.class, order.next());
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
     * @return 初始化顺序。如果参数为 Null 或者找不到顺序，则返回 {@link Integer#MAX_VALUE} 值。
     */
    static Integer find(Object obj) {
        return Optional.ofNullable(obj)
                .map(Object::getClass)
                .map(Class::getName)
                .map(INITIALIZER_TO_ORDER::get)
                .orElse(Integer.MAX_VALUE);
    }

    /**
     * 初始化器排序步骤。
     *
     * @see org.springframework.security.config.annotation.web.builders.FilterOrderRegistration.Step
     */
    private static class Step {

        private int value;
        private final int stepSize;

        Step(int initialValue, int stepSize) {
            this.value = initialValue;
            this.stepSize = stepSize;
        }

        int next() {
            int value = this.value;
            this.value += this.stepSize;
            return value;
        }
    }

}
