package com.github.mrzhqiang.rowing.system.data;

import java.io.File;

public interface DataDictService {

    /**
     * 字典组的 Sheet 名称。
     */
    String GROUP_SHEET_NAME = "group";
    /**
     * 字典项的 Sheet 名称。
     */
    String ITEM_SHEET_NAME = "item";

    /**
     * 通过导入 Excel 文件生成数据字典相关实体数据。
     * <p>
     * 注意：这个操作将删除所有内置的数据字典数据。
     *
     * @param excelFile 内置数据字典的 Excel 文件。
     */
    void importExcel(File excelFile);
}
