package com.github.mrzhqiang.rowing.dict;

/**
 * 字典服务。
 */
public interface DictService {

    /**
     * 字典组的 Sheet 名称。
     */
    String GROUP_SHEET_NAME = "group";
    /**
     * 字典项的 Sheet 名称。
     */
    String ITEM_SHEET_NAME = "item";

    /**
     * 同步内置字典。
     *
     * @param basePackage 基础包，即字典枚举类所在包路径。
     */
    void syncInternal(String basePackage);

    /**
     * 同步 Excel 字典。
     *
     * @param excelFile 内置字典 Excel 文件。
     */
    void syncExcel(String excelFile);
}
