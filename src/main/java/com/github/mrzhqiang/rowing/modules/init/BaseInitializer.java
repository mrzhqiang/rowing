package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.helper.Exceptions;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.ClassUtils;

/**
 * 基础的初始化器。
 * <p>
 * 主要封装了公共逻辑，避免重复代码。
 */
@Slf4j
public abstract class BaseInitializer implements Initializer {

    protected InitTaskRepository repository;
    protected InitTaskLogRepository logRepository;
    protected MessageSourceAccessor sourceAccessor;

    /**
     * 方法注入初始化任务仓库。
     * <p>
     * 注意：对于这种注入方式，应尽可能少用。
     */
    @Autowired
    public void setRepository(InitTaskRepository repository) {
        this.repository = repository;
    }

    /**
     * 方法注入初始化任务日志仓库。
     * <p>
     * 注意：对于这种注入方式，应尽可能少用。
     */
    @Autowired
    public void setLogRepository(InitTaskLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * 方法注入消息源。
     * <p>
     * 注意：对于这种注入方式，应尽可能少用。
     */
    @Autowired
    public final void setMessageSource(MessageSource messageSource) {
        this.sourceAccessor = new MessageSourceAccessor(messageSource);
    }

    /**
     * 获取名称。
     * <p>
     * 通过路径从国际化消息中获取对应的名称，如果不存在，则使用实现类的简单名称。
     *
     * @return 初始化器的名称。不会返回 Null 值。
     */
    public final String getName() {
        // 根据 ServletRequest 的 getLocale 方法指示：
        // 当请求头中包含 Accept-Language 值时，返回指定的语言代码，否则返回系统默认语言代码
        // Spring 包装的 SavedRequest 会将返回的语言代码保存到本地线程中
        // 当通过下面的方法获取国际化消息内容时，自动从本地线程中取得请求对应的语言代码，从而获得对应语言的国际化内容
        return sourceAccessor.getMessage(getPath(), ClassUtils.getShortName(getClass()));
    }

    /**
     * 尝试执行任务。
     *
     * @param task 初始化任务。
     */
    void attemptExecute(InitTask task) {
        if (task == null || !task.isExecutable()) {
            log.warn(sourceAccessor.getMessage("Initializer.execute.ignored",
                    "无效的初始化任务，忽略执行"));
            return;
        }

        String name = getName();
        String path = getPath();
        String startMessage = sourceAccessor.getMessage("Initializer.execute.started",
                new Object[]{name, path},
                Strings.lenientFormat("准备执行初始化任务：{}--[{}]", name, path));
        log.info(startMessage);

        String taskName = task.getName();
        task.setStatus(TaskStatus.STARTED);
        InitTaskLog taskLog = InitTaskLog.of(task);
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            this.execute();
            task.setStatus(TaskStatus.COMPLETED);
            Stopwatch stop = stopwatch.stop();
            String successMessage = sourceAccessor.getMessage("Initializer.execute.success",
                    new Object[]{taskName, stop},
                    Strings.lenientFormat("初始化任务 %s 执行成功，用时：%s", taskName, stop));
            taskLog.setMessage(successMessage);
            log.info(successMessage);
        } catch (InitializeException e) {
            task.setStatus(TaskStatus.FAILED);
            String cause = Exceptions.ofMessage(e);
            String trace = Exceptions.ofTrace(e);
            String failedMessage = sourceAccessor.getMessage("Initializer.execute.failure",
                    new Object[]{taskName, cause},
                    Strings.lenientFormat("初始化任务 %s 执行失败，原因：%s", taskName, cause));
            taskLog.setMessage(failedMessage);
            taskLog.setTrace(trace);
            // 抛出异常，中断行为，但不会影响任务状态和任务日志的记录
            throw new RuntimeException(failedMessage, e);
        } finally {
            repository.save(task);
            logRepository.save(taskLog);
        }
    }
}
