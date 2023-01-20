package com.github.mrzhqiang.rowing.modules.setting;

import com.github.mrzhqiang.rowing.modules.init.BaseInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 系统设置自动初始化器。
 */
@Slf4j
@Component
public class SysSettingInitializer extends BaseInitializer {

    /**
     * 系统设置 Excel 文件地址。
     */
    private static final String EXCEL_FILE_LOCATION = ResourceUtils.CLASSPATH_URL_PREFIX + "data/sys-setting.xlsx";

    private final SysSettingService service;

    public SysSettingInitializer(SysSettingService service) {
        this.service = service;
    }

    @Override
    public void execute() {
        File excelFile = null;
        try {
            excelFile = ResourceUtils.getFile(EXCEL_FILE_LOCATION);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        service.importExcel(excelFile);
    }
}
