package com.github.mrzhqiang.rowing.system;

import com.github.mrzhqiang.rowing.system.data.DataDictService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;

@Slf4j
@Component
public class DataDictAutoInitializer extends BaseAutoInitializer {

    private static final String EXCEL_FILE_LOCATION = ResourceUtils.CLASSPATH_URL_PREFIX + "data/data-dict.xlsx";

    private final DataDictService groupService;

    public DataDictAutoInitializer(DataDictService groupService) {
        this.groupService = groupService;
    }

    @Override
    public void execute() throws Exception {
        File excelFile = ResourceUtils.getFile(EXCEL_FILE_LOCATION);
        groupService.importExcel(excelFile);
    }
}
