package com.github.mrzhqiang.rowing.core.init;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽象的基础自动初始化器。
 * <p>
 * 主要是实现一些重复的逻辑，避免过多的样板代码，同时满足里氏替换原则。
 */
public abstract class BaseAutoInitializer implements AutoInitializer {

    protected SysInitRepository initRepository;

    @Override
    public boolean hasInitialized() {
        return initRepository.findByName(this.getName())
                .map(SysInit::hasFinished)
                .orElse(false);
    }

    @Override
    public void onFinished() {
        initRepository.findByName(this.getName()).ifPresent(it -> {
            it.setStatus(SysInit.Status.FINISHED);
            initRepository.save(it);
        });
    }

    @Autowired
    public void setInitRepository(SysInitRepository initRepository) {
        this.initRepository = initRepository;
    }
}
