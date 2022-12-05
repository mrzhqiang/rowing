package com.github.mrzhqiang.rowing.module.setting;

import java.io.File;

public interface SysSettingService {

    String GROUP_SHEET_NAME = "group";
    String ITEM_SHEET_NAME = "item";

    /**
     * 通过导入 Excel 文件生成系统设置相关实体数据。
     * <p>
     * 注意：这个操作将删除所有系统设置数据。
     *
     * @param excelFile 包含系统设置数据的内置 Excel 文件。
     */
    void importExcel(File excelFile);
}
