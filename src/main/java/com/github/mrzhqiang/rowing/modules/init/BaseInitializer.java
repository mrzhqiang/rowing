package com.github.mrzhqiang.rowing.modules.init;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import javax.annotation.Nonnull;

/**
 * 基础的初始化器。
 * <p>
 * 主要封装了公共逻辑，避免重复代码。
 */
@Slf4j
public abstract class BaseInitializer implements Initializer {

    protected InitTaskRepository repository;
    protected MessageSourceAccessor sourceAccessor;

    /**
     * 方法注入系统初始化仓库。
     * <p>
     * 注意：对于这种注入方式，应尽可能少用。
     */
    @Autowired
    public final void setRepository(InitTaskRepository repository) {
        this.repository = repository;
    }

    /**
     * 方法注入消息源。
     * <p>
     * 注意：对于这种注入方式，应尽可能少用。
     * <p>
     * 注入的消息源是国际化消息文件，我们用消息源访问器包装起来，优先访问多语言数据表，只有不存在的情况下，才使用注入的消息源。
     *
     * @param messageSource 国际化消息源。属于系统内置的消息源，通过添加多语言数据表映射可以在线修改国际化消息内容。
     */
    @Autowired
    public final void setMessageSource(MessageSource messageSource) {
        // todo 封装 database 数据库查询
        this.sourceAccessor = new MessageSourceAccessor(messageSource);
    }

    /**
     * 初始化器名称。
     * <p>
     * 1. 一般是多语言名称，即从多语言数据表中查询 {@link #getPath() 路径} 对应的名称；
     * <p>
     * 2. 如果在多语言表中不存在，那么将从国际化消息文件中获取名称；
     * <p>
     * 3. 如果在国际化消息文件中也不存在，则使用保底的初始化实现类的 {@link InitTask#getName() 简称}。
     *
     * @return 支持多语言及国际化消息的初始化名称。
     */
    @Nonnull
    public final String getName() {
        // 根据 ServletRequest 的 getLocale 方法指示：
        // 当请求头中包含 Accept-Language 值时，返回指定的语言代码，否则返回系统默认语言代码
        // Spring 包装的 SavedRequest 会将返回的语言代码保存到本地线程中
        // 当通过下面的方法获取国际化消息内容时，自动从本地线程中取得请求对应的语言代码，从而获得对应语言的国际化内容
        return sourceAccessor.getMessage(this.getPath(), this.getClass().getSimpleName());
    }

    /**
     * 尝试执行初始化任务。
     *
     * @param task 初始化任务实例。
     */
    protected final void attemptExecute(InitTask task) {
        try {
            task.withStarted();
            this.execute();
            task.withCompleted();
            log.info("初始化任务 {} 执行成功", task.getName());
        } catch (Exception e) {
            task.withFailed(e);
            String message = Strings.lenientFormat("初始化任务 %s 执行失败", task.getName());
            // 抛出异常，中止启动，但不影响事务回滚
            throw new RuntimeException(message, e);
        } finally {
            repository.save(task);
        }
    }
}
