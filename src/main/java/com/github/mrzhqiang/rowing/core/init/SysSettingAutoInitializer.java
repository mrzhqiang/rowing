package com.github.mrzhqiang.rowing.core.init;

import com.github.mrzhqiang.rowing.core.setting.SysSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * 系统设置自动初始化器。
 */
@Slf4j
@Component
public class SysSettingAutoInitializer extends BaseAutoInitializer {

    /**
     * 系统设置 Excel 文件地址。
     */
    private static final String EXCEL_FILE_LOCATION = ResourceUtils.CLASSPATH_URL_PREFIX + "data/sys-setting.xlsx";

    private final SysSettingService service;

    public SysSettingAutoInitializer(SysSettingService service) {
        this.service = service;
    }

    @Override
    public void execute() throws Exception {
        File excelFile = ResourceUtils.getFile(EXCEL_FILE_LOCATION);
        service.importExcel(excelFile);
    }
}
