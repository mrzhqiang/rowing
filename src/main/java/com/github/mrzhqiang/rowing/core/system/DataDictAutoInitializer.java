package com.github.mrzhqiang.rowing.core.system;

import com.github.mrzhqiang.rowing.core.system.data.DataDictService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class DataDictAutoInitializer extends BaseAutoInitializer {

    private final DataDictService groupService;

    @Value(ResourceUtils.CLASSPATH_URL_PREFIX + "data/data-dict.xlsx")
    private Resource resource;

    public DataDictAutoInitializer(DataDictService groupService) {
        this.groupService = groupService;
    }

    @Override
    public void attemptInitialize() {
        try {
            File excelFile = resource.getFile();
            groupService.importExcel(excelFile);
        } catch (IOException e) {
            String message = Strings.lenientFormat("获取内置的数据字典文件出错");
            throw new RuntimeException(message, e);
        }
    }
}
