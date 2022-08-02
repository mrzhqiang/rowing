package com.github.mrzhqiang.rowing.api.system.data;

import java.io.File;

public interface DataDictService {

    /**
     * 通过导入 Excel 文件生成数据字典相关实体数据。
     * <p>
     * 注意：这个操作将删除所有数据字典数据。
     *
     * @param excelFile 包含数据字典数据的内置 Excel 文件。
     */
    void importExcel(File excelFile);
}
