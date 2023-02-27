package com.github.mrzhqiang.rowing.modules.menu;

import com.github.mrzhqiang.rowing.modules.init.AutoInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MenuAutoInitializer extends AutoInitializer {

    @Override
    protected void autoRun() throws Exception {

    }

    @Override
    public boolean isSupportRepeat() {
        return false;
    }
}
