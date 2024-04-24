package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Enums;

/**
 * 字典类型。
 * <p>
 * 主要用于字典组的分类。
 * <p>
 * 同时本身作为字典枚举，也会被扫描到数据库中，作为内置类型的字典。
 * <p>
 * 对于字典枚举来说，基于 Spring Data Rest 框架可以通过 rest-messages_**.properties 国际化消息文件，进行枚举值的国际化标签。
 * <p>
 * 以中文简单举例，在 rest-messages_zn_CN.properties 文件中提供：
 * <p>
 * com.github.mrzhqiang.rowing.domain.DictType.INTERNAL=内置字典
 * <p>
 * 则在访问 REST 接口时，返回的数据如果包含 DictType.INTERNAL 值，则自动翻译为【内置字典】。
 */
public enum DictType {

    /**
     * 内置类型。
     * <p>
     * 内置字典，通过类扫描器扫描当前枚举类所在包下的相关枚举类，并转为内置字典存储到数据库。
     */
    INTERNAL,
    /**
     * Excel 类型。
     * <p>
     * 通过 Excel 文件导入字典，存储到数据库。
     */
    EXCEL,
    /**
     * 自定义类型。
     * <p>
     * 通过字典模块进行 CURD 管理。
     */
    CUSTOM,
    ;

    public static DictType of(String type) {
        return Enums.findByNameIgnoreCase(DictType.class, type, INTERNAL);
    }

}
