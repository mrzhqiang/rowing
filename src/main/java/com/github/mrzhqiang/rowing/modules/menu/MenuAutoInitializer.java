package com.github.mrzhqiang.rowing.modules.menu;

import com.github.mrzhqiang.rowing.modules.init.Initializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MenuAutoInitializer implements Initializer {

    @Override
    public void execute() {
        // 搜索对应接口并记录到数据库，应该支持重复执行
    }

    @Override
    public boolean isAutoExecute() {
        return false;
    }
}
