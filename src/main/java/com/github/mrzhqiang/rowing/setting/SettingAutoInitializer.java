package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.init.AutoInitializer;
import com.github.mrzhqiang.rowing.init.AutoInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * 设置自动初始化器。
 */
@Slf4j
//@Component
public class SettingAutoInitializer extends AutoInitializer {

    /**
     * 系统设置 Excel 文件地址。
     */
    private static final String EXCEL_FILE_LOCATION = ResourceUtils.CLASSPATH_URL_PREFIX + "data/setting.xlsx";

    private final SettingService service;

    public SettingAutoInitializer(SettingService service) {
        this.service = service;
    }

    @Override
    protected void autoRun() throws Exception {
        File excelFile = ResourceUtils.getFile(EXCEL_FILE_LOCATION);
        service.importExcel(excelFile);
    }

    @Override
    public boolean isSupportRepeat() {
        return false;
    }
}
