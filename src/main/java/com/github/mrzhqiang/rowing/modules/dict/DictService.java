package com.github.mrzhqiang.rowing.modules.dict;

import java.io.File;

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
     * 通过导入 Excel 文件生成字典相关实体数据。
     *
     * @param excelFile 内置字典 Excel 文件。
     */
    void importExcel(File excelFile);
}
