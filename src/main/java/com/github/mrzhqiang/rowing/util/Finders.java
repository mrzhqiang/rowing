package com.github.mrzhqiang.rowing.util;

import org.springframework.boot.ApplicationArguments;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

/**
 * 查询器。
 */
public final class Finders {
    private Finders() {
        // no instances.
    }

    /**
     * 查询是否存在指定参数并且参数为 true 值。
     *
     * @param args       Spring Application 参数。
     * @param optionName 选项名称。
     * @return 返回 true 表示存在指定参数并且参数为真；返回 false 表示不存在指定参数，或参数为假。
     */
    public static boolean hasTrue(ApplicationArguments args, String optionName) {
        return hasValue(args, optionName, Boolean.TRUE.toString());
    }

    /**
     * 查询是否存在指定参数并且参数为指定值。
     *
     * @param args       Spring Application 参数。
     * @param optionName 选项名称。
     * @param value      指定值。
     * @return 返回 true 表示存在指定参数并且参数为指定值；返回 false 表示不存在指定参数，或参数不为指定值。
     */
    public static boolean hasValue(ApplicationArguments args, String optionName, String value) {
        return Optional.ofNullable(args)
                // 从运行参数或环境变量中判断是否包含指定参数
                .filter(it -> it.containsOption(optionName))
                .map(it -> it.getOptionValues(optionName))
                .filter(it -> !CollectionUtils.isEmpty(it))
                // 解析选项参数，判断是否存在指定值
                .map(it -> it.contains(value))
                .isPresent();
    }
}
