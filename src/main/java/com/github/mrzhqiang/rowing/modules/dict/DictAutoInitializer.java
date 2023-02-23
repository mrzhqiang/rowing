package com.github.mrzhqiang.rowing.modules.dict;

import com.github.mrzhqiang.rowing.domain.DictType;
import com.github.mrzhqiang.rowing.modules.init.AutoInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * 字典自动初始化执行器。
 */
@Slf4j
@Component
public class DictAutoInitializer extends AutoInitializer {

    /**
     * 字典 Excel 文件地址。
     */
    private static final String EXCEL_FILE_LOCATION = ResourceUtils.CLASSPATH_URL_PREFIX + "data/dict.xlsx";

    private final DictService service;

    public DictAutoInitializer(DictService service) {
        this.service = service;
    }

    @Override
    protected void autoRun() throws Exception {
        // 通过字典类型枚举，获取指定包路径，同步此包下的所有枚举作为内置字典
        String basePackage = DictType.class.getPackage().getName();
        service.syncInternal(basePackage);

        // 通过 Excel 文件，导入所有内容作为 Excel 字典
        File excelFile = ResourceUtils.getFile(EXCEL_FILE_LOCATION);
        service.importExcel(excelFile);
    }
}
