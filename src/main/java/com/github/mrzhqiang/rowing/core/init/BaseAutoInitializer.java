package com.github.mrzhqiang.rowing.core.init;

import com.github.mrzhqiang.rowing.core.exception.InitializeException;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

/**
 * 基础的自动初始化器。
 * <p>
 * 主要封装公共逻辑，避免代码重复。
 */
@Slf4j
public abstract class BaseAutoInitializer implements AutoInitializer {

    protected SysInitRepository repository;

    /**
     * 方法注入系统初始化仓库。
     * <p>
     * 注意：对于这种注入方式，应尽可能少用。
     */
    @Autowired
    public void setRepository(SysInitRepository repository) {
        this.repository = repository;
    }

    /**
     * 初始化名称。
     * <p>
     * 实现类必须保证此名称的全局唯一性。
     * <p>
     * 这里返回的是扩展类的全限定类名，因此不要轻易修改扩展类的包名，否则会导致旧数据不可用。
     */
    @Nonnull
    @Override
    public final String getName() {
        return this.getClass().getName();
    }

    @Override
    public final void run(String... args) {
        String name = getName();
        log.info("准备执行初始化任务：{}", name);
        repository.findByName(name)
                .filter(it -> !it.isCompleted())
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
