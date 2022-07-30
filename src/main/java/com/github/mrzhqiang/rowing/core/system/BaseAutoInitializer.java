package com.github.mrzhqiang.rowing.core.system;

import com.github.mrzhqiang.rowing.core.system.init.SysInit;
import com.github.mrzhqiang.rowing.core.system.init.SysInitRepository;
import com.google.common.base.Stopwatch;
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

    protected SysInitRepository initRepository;

    /**
     * 方法注入系统初始化仓库。
     * <p>
     * 提示：对于这种注入方式，应尽可能少用。
     */
    @Autowired
    public void setInitRepository(SysInitRepository initRepository) {
        this.initRepository = initRepository;
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
        return initRepository.findByName(this.getName())
                .map(SysInit::hasFinished)
                .orElse(false);
    }

    @Transactional
    @Override
    public void run(String... args) {
        String name = this.getName();
        Stopwatch stopwatch = Stopwatch.createStarted();
        log.info("开始检测 {} 是否已执行初始化", name);
        if (hasInitialized()) {
            log.info("检测到 {} 已执行初始化，准备跳过执行，耗时：{}", name, stopwatch.stop());
            return;
        }

        log.info("检测到 {} 未完成初始化，将尝试执行", name);
        try {
            this.attemptInitialize();
        } catch (Exception e) {
            String message = Strings.lenientFormat("执行失败！%s 初始化出现问题，耗时：%s", name, stopwatch.stop());
            log.error(message, e);
            throw new RuntimeException(e);
        }

        initRepository.findByName(name).ifPresent(it -> {
            it.setStatus(SysInit.Status.FINISHED);
            initRepository.save(it);
        });
        log.info("执行成功！{} 已完成初始化，耗时：{}", name, stopwatch.stop());
    }

    @Override
    public final int getOrder() {
        return AutoInitializationOrderRegistration.find(this);
    }
}
