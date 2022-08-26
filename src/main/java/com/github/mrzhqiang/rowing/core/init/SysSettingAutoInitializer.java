package com.github.mrzhqiang.rowing.core.init;

import com.github.mrzhqiang.rowing.core.setting.SysSettingService;
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
    public void execute() throws Exception {
        File excelFile = ResourceUtils.getFile(EXCEL_FILE_LOCATION);
        service.importExcel(excelFile);
    }
}
