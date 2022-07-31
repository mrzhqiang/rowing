package com.github.mrzhqiang.rowing.core.system;

import com.github.mrzhqiang.rowing.core.system.init.SysInitService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

/**
 * 抽象的基础自动初始化器。
 * <p>
 * 主要是实现一些重复的逻辑，避免过多的样板代码，同时满足里氏替换原则。
 */
@Slf4j
public abstract class BaseAutoInitializer implements AutoInitializer {

    protected SysInitService sysInitService;

    /**
     * 方法注入系统初始化服务。
     * <p>
     * 提示：对于这种注入方式，应尽可能少用。
     */
    @Autowired
    public void setSysInitService(SysInitService sysInitService) {
        this.sysInitService = sysInitService;
    }

    /**
     * 自动初始化器的名称。
     * <p>
     * 这里返回的是扩展类的全限定类名，因此不要轻易修改扩展类的包名，否则会导致旧数据不可用。
     *
     * @return 返回自动初始化器的名称，需保证全局唯一性，不能返回 Null 值。
     */
    @Nonnull
    @Override
    public final String getName() {
        return this.getClass().getName();
    }

    @Override
    public final boolean hasInitialized() {
        String name = getName();
        return sysInitService.isFinishedBy(name);
    }

    @Transactional
    @Override
    public void run(String... args) {
        if (hasInitialized()) {
            return;
        }

        String name = getName();
        try {
            this.attemptInitialize();
        } catch (Exception e) {
            String message = Strings.lenientFormat("执行失败！%s 初始化出现问题", name);
            log.error(message, e);
            throw new RuntimeException(e);
        }

        sysInitService.updateFinishedBy(name);
    }

    @Override
    public final int getOrder() {
        return AutoInitializationOrderRegistration.find(this);
    }
}
