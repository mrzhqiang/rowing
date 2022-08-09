package com.github.mrzhqiang.rowing.system;

import com.github.mrzhqiang.rowing.system.setting.SysSettingService;
import com.github.mrzhqiang.rowing.system.setting.SysSettingService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;

@Slf4j
@Component
public class SysSettingAutoInitializer extends BaseAutoInitializer {

    private static final String EXCEL_FILE_LOCATION = ResourceUtils.CLASSPATH_URL_PREFIX + "data/sys-setting.xlsx";

    private final SysSettingService service;

    public SysSettingAutoInitializer(SysSettingService service) {
        this.service = service;
    }

    @Override
    public void attemptInitialize() {
        try {
            File excelFile = ResourceUtils.getFile(EXCEL_FILE_LOCATION);
            service.importExcel(excelFile);
        } catch (Exception e) {
            String message = Strings.lenientFormat("初始化系统设置 %s 失败", EXCEL_FILE_LOCATION);
            throw new RuntimeException(message, e);
        }
    }
}
