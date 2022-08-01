package com.github.mrzhqiang.rowing.core.system.data;

import java.io.File;

public interface DataDictService {

    /**
     * 通过导入 Excel 文件生成数据字典组的实体列表。
     * <p>
     * 为了保证内置数据的完整性，这个操作将首先清理所有数据字典组以及数据字典项。
     *
     * @param excelFile 包含数据字典数据的 Excel 文件。
     */
    void importExcel(File excelFile);
}
