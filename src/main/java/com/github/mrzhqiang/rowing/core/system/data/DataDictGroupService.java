package com.github.mrzhqiang.rowing.core.system.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface DataDictGroupService {

    /**
     * 通过表单创建实体。
     *
     * @param form 分组表单。
     * @return 创建的分组实体。
     */
    DataDictGroup create(DataDictGroupForm form);

    /**
     * 通过代码删除实体。
     *
     * @param code 分组代码。
     */
    void deleteByCode(String code);

    /**
     * 删除所有实体。
     */
    void deleteAll();

    /**
     * 通过代码查询数据。
     *
     * @param code 分组代码。
     * @return 分组数据。
     */
    DataDictGroupData findByCode(String code);

    /**
     * 通过表单更新指定代码的实体。
     *
     * @param code 分组代码。
     * @param form 分组表单。
     * @return 更新后的分组实体。
     */
    DataDictGroup update(String code, DataDictGroupForm form);

    /**
     * 列出全部实体。
     *
     * @return 分组实体列表。
     */
    List<DataDictGroupData> list();

    /**
     * 通过可分页参数查询分页数据。
     *
     * @param pageable 可分页参数。
     * @return 分页数据。
     */
    Page<DataDictGroupData> page(Pageable pageable);

    /**
     * 导入 Excel 文件生成数据字典分组。
     *
     * @param excelFile 包含数据字典数据的 Excel 文件。
     * @return 使用 code 作为 key 字符串，映射的数据字典实体。
     */
    Map<String, DataDictGroup> importExcel(File excelFile);
}
