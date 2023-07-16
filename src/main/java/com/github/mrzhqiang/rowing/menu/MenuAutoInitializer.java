package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.account.RunAsSystem;
import com.github.mrzhqiang.rowing.init.AutoInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 菜单自动初始化器。
 */
@Slf4j
@Component
public class MenuAutoInitializer extends AutoInitializer {

    @RunAsSystem
    @Override
    protected void onExecute() {

    }

}
