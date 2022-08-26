package com.github.mrzhqiang.rowing.core.domain;

import com.github.mrzhqiang.rowing.core.data.DataDictScanner;

/**
 * 数据字典类型。
 * <p>
 * 主要用于 {@link com.github.mrzhqiang.rowing.core.data.DataDictGroup 数据字典组} 的分类。
 * <p>
 * 同时作为数据字典枚举，本身也会被扫描到数据库中，作为内置数据字典。
 * <p>
 * 对于数据字典枚举来说，基于 Spring Data Rest 框架可以通过 rest-message-**.properties 国际化消息文件，进行枚举值的国际化标签。
 * <p>
 * 以中文简单举例，在 rest-message-zn_CN.properties 文件中提供：DataDictType.INTERNAL=内置数据字典，则在访问 rest 接口时，返回枚举值对应的国际化名称。
 */
public enum DataDictType {

    /**
     * 内置类型。
     * <p>
     * 内置类型的数据字典，通过 {@link DataDictScanner 数据字典扫描器} 扫描包下的所有枚举类，将枚举名称作为数据字典组以及将枚举字段作为数据字典项并添加到数据库。
     */
    INTERNAL,
    /**
     * 自定义类型。
     * <p>
     * 自定义类型的数据字典，通过数据字典模块进行 CURD 管理。
     */
    CUSTOMIZE,
}
