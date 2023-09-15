package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.dict.ClassScanner;
import com.github.mrzhqiang.rowing.dict.DictGroup;

/**
 * 字典类型。
 * <p>
 * 主要用于 {@link DictGroup} 的分类。
 * <p>
 * 同时作为字典枚举，本身也会被扫描到数据库中，作为内置字典的一员。
 * <p>
 * 对于字典枚举来说，基于 Spring Data Rest 框架可以通过 rest-messages_**.properties 国际化消息文件，进行枚举值的国际化标签。
 * <p>
 * 以中文简单举例，在 rest-messages_zn_CN.properties 文件中提供：
 * <p>
 * com.github.mrzhqiang.rowing.domain.DataDictType.INTERNAL=内置字典
 * <p>
 * 则在访问 rest 接口时，返回枚举值对应的国际化名称。
 */
public enum DictType {

    /**
     * 内置类型。
     * <p>
     * 内置字典，通过 {@link ClassScanner} 扫描当前枚举类所在包下的相关枚举类，并转为内置字典存储到数据库。
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

}
