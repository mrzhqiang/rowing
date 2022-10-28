package com.github.mrzhqiang.rowing.system.init;

import com.github.mrzhqiang.rowing.basic.exception.InitializeException;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import javax.annotation.Nonnull;

/**
 * 基础的自动初始化器。
 * <p>
 * 主要是筛选未完成的初始化器列表，自动执行初始化任务。
 * <p>
 * 当自动初始化执行成功，自动标记为已完成，当自动初始化执行失败，自动标记为失败。
 * <p>
 * 同时固定实现返回名称和返回排序的方法，以统一规则。
 */
@Slf4j
public abstract class BaseAutoInitializer implements AutoInitializer {

    protected SysInitRepository repository;
    protected MessageSourceAccessor sourceAccessor;

    /**
     * 方法注入系统初始化仓库。
     * <p>
     * 注意：对于这种注入方式，应尽可能少用。
     */
    @Autowired
    public final void setRepository(SysInitRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public final void setMessageSource(MessageSource messageSource) {
        this.sourceAccessor = new MessageSourceAccessor(messageSource);
    }

    @Nonnull
    @Override
    public final String getName() {
        return sourceAccessor.getMessage(this.getPath(), this.getClass().getSimpleName());
    }

    @Nonnull
    @Override
    public final String getPath() {
        return this.getClass().getName();
    }

    @Override
    public final void run(String... args) {
        String path = getPath();
        log.info("准备执行初始化任务：{} 路径：{}", this.getName(), path);
        repository.findByPath(path)
                .filter(SysInit::isNotCompleted)
                .ifPresent(this::attemptExecute);
    }

    private void attemptExecute(SysInit it) {
        try {
            it.withStarted();
            execute();
            it.withCompleted();
            log.info("初始化任务 {} 执行成功", it.getName());
        } catch (Exception e) {
            it.withFailed(e);
            String message = Strings.lenientFormat("初始化任务 %s 执行失败", it.getName());
            // 抛出异常，中止启动，但不影响事务回滚
            throw new InitializeException(message, e);
        } finally {
            repository.save(it);
        }
    }

    @Override
    public final int getOrder() {
        // 从注册登记中获得扩展实现的执行顺序，所以有严格要求的自动初始化必须注册登记
        return AutoInitializationOrderRegistration.find(this);
    }
}
