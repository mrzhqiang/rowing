package com.github.mrzhqiang.rowing.system;

import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.system.exception.InitializeException;
import com.github.mrzhqiang.rowing.system.init.SysInit;
import com.github.mrzhqiang.rowing.system.init.SysInitRepository;
import com.github.mrzhqiang.rowing.util.Exceptions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.time.Instant;

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
        repository.findByName(name).filter(this::isNotCompleted)
                .ifPresent(it -> {
                    it.setStatus(TaskStatus.STARTED);
                    it.setStartTime(Instant.now());

                    try {
                        execute();
                        it.setStatus(TaskStatus.COMPLETED);
                        it.setEndTime(Instant.now());
                        log.info("初始化任务 {} 执行成功", name);
                    } catch (Exception e) {
                        it.setStatus(TaskStatus.FAILED);
                        it.setEndTime(Instant.now());
                        it.setErrorMessage(Exceptions.ofMessage(e));
                        it.setErrorTrace(Exceptions.ofTrace(e));
                        String message = Strings.lenientFormat("初始化任务 %s 执行失败", it.getName());
                        // 抛出异常，中止启动，但不影响事务回滚
                        throw new InitializeException(message, e);
                    } finally {
                        repository.save(it);
                    }
                });
    }

    private boolean isNotCompleted(SysInit entity) {
        boolean completed = TaskStatus.COMPLETED.equals(entity.getStatus());
        if (completed) {
            log.info("初始化任务 {} 已完成，跳过执行", entity.getName());
        }
        return !completed;
    }

    @Override
    public final int getOrder() {
        // 从注册登记中获得扩展实现的执行顺序，所以有严格要求的自动初始化必须注册登记
        return AutoInitializationOrderRegistration.find(this);
    }
}
