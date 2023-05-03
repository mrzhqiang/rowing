package com.github.mrzhqiang.rowing.util;

import org.springframework.boot.ApplicationArguments;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 环境工具。
 */
public final class Environments {
    private Environments() {
        // no instances.
    }

    /**
     * 判断是否存在指定参数并且参数为 true 值。
     *
     * @param args       Spring Application 参数。
     * @param optionName 选项名称。
     * @return 返回 true 表示存在指定参数并且参数为真；返回 false 表示不存在指定参数，或参数为假。
     */
    public static boolean hasTrue(ApplicationArguments args, String optionName) {
        return hasValue(args, optionName, Boolean.TRUE.toString());
    }

    /**
     * 判断是否存在指定参数并且参数为指定值。
     *
     * @param args       Spring Application 参数。
     * @param optionName 选项名称。
     * @param value      指定值。
     * @return 返回 true 表示存在指定参数并且参数为指定值；返回 false 表示不存在指定参数，或参数不为指定值。
     */
    public static boolean hasValue(ApplicationArguments args, String optionName, String value) {
        // 从运行参数或环境变量中判断是否包含指定参数
        if (args.containsOption(optionName)) {
            // 解析选项参数，判断是否存在 true
            List<String> optionValues = args.getOptionValues(optionName);
            return !CollectionUtils.isEmpty(optionValues) && optionValues.contains(value);
        }
        return false;
    }
}
