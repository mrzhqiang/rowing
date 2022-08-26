package com.github.mrzhqiang.rowing.core.data;

public interface DataDictService {

    /**
     * 同步内置数据字典。
     *
     * @param basePackage 基础包，即数据字典枚举类所在包路径。
     */
    void syncInternal(String basePackage);
}
