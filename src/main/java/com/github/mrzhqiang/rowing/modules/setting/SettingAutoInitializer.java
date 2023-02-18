package com.github.mrzhqiang.rowing.modules.setting;

import com.github.mrzhqiang.rowing.modules.init.InitializationException;
import com.github.mrzhqiang.rowing.modules.init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * 设置自动初始化器。
 */
@Slf4j
@Component
public class SettingAutoInitializer implements Initializer {

    /**
     * 系统设置 Excel 文件地址。
     */
    private static final String EXCEL_FILE_LOCATION = ResourceUtils.CLASSPATH_URL_PREFIX + "data/setting.xlsx";

    private final SysSettingService service;

    public SettingAutoInitializer(SysSettingService service) {
        this.service = service;
    }

    @Override
    public void execute() {
        try {
            File excelFile = ResourceUtils.getFile(EXCEL_FILE_LOCATION);
            service.importExcel(excelFile);
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }

    @Override
    public boolean isAutoExecute() {
        return false;
    }
}
