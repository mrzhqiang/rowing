package com.github.mrzhqiang.rowing.core.system.data;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface DataDictService {

    /**
     * 通过导入 Excel 文件生成数据字典组的实体列表。
     * <p>
     * 为了保证内置数据的完整性，这个操作将首先清理所有数据字典组以及数据字典项。
     *
     * @param excelFile 包含数据字典数据的 Excel 文件。
     * @return Map 对象，key 表示组代码，value 表示数据字典组。
     */
    Map<String, DataDictGroup> importExcel(File excelFile);
}
