package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.helper.Exceptions;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskMode;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.domain.TaskType;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.util.Finders;
import com.github.mrzhqiang.rowing.util.Validations;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InitTaskServiceJpaImpl implements InitTaskService {

    private final InitTaskMapper mapper;
    private final InitTaskRepository repository;
    private final InitTaskLogRepository logRepository;
    private final List<Initializer> initializers;

    public InitTaskServiceJpaImpl(InitTaskMapper mapper,
                                  InitTaskRepository repository,
                                  InitTaskLogRepository logRepository,
                                  List<Initializer> initializers) {
        this.mapper = mapper;
        this.repository = repository;
        this.logRepository = logRepository;
        this.initializers = initializers;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync(ApplicationArguments args) {
        // 从运行参数或环境变量中判断是否包含指定参数
        if (Finders.hasTrue(args, SKIP_SYNC_ARGS_NAME)) {
            // 根据 ServletRequest 的 getLocale 方法指示：
            // 当请求头中包含 Accept-Language 值时，返回指定的语言代码，否则返回系统默认语言代码
            // Spring 包装的 SavedRequest 会将返回的语言代码保存到本地线程中
            // 当通过下面的方法获取国际化消息内容时，自动从本地线程中取得请求对应的语言代码，从而获得对应语言的国际化内容
            log.info(I18nHolder.getAccessor().getMessage(
                    "InitTaskService.sync.skipMessage", new Object[]{SKIP_SYNC_ARGS_NAME},
                    Strings.lenientFormat("发现 --%s=true 将跳过同步数据", SKIP_SYNC_ARGS_NAME)));
            return;
        }

        // 找到未被记录的初始化任务实现，进行新增
        List<InitTask> addedList = initializers.stream()
                .filter(it -> !repository.existsByPath(it.getPath()))
                .map(this::convertEntity)
                .map(repository::save)
                .collect(Collectors.toList());

        // 找到已删除的初始化任务实现，进行废弃
        List<String> paths = initializers.stream()
                .map(Initializer::getPath)
                .collect(Collectors.toList());
        List<InitTask> discardList = Collections.emptyList();
        if (!CollectionUtils.isEmpty(paths)) {
            discardList = repository.findAllByDiscardAndPathNotIn(Logic.NO, paths).stream()
                    .peek(it -> it.setDiscard(Logic.YES))
                    .map(repository::save)
                    .collect(Collectors.toList());
        }

        // 打印简要的初始化报告
        log.info(I18nHolder.getAccessor().getMessage(
                "InitTaskService.sync.simpleReport", new Object[]{addedList.size(), discardList.size()},
                Strings.lenientFormat("新增 %s 个初始化任务，废弃 %s 初始化任务",
                        addedList.size(), discardList.size())));

        // 如果开启调试日志，则打印初始化报告详情
        if (log.isDebugEnabled()) {
            if (!addedList.isEmpty()) {
                log.debug(I18nHolder.getAccessor().getMessage(
                        "InitTaskService.sync.addReport", new Object[]{addedList},
                        Strings.lenientFormat("新增初始化任务详情：%s", addedList)));
            }
            if (!discardList.isEmpty()) {
                log.debug(I18nHolder.getAccessor().getMessage(
                        "InitTaskService.sync.discardReport", new Object[]{discardList},
                        Strings.lenientFormat("废弃初始化任务详情：%s", discardList)));
            }
        }
    }

    private InitTask convertEntity(Initializer initializer) {
        InitTask entity = mapper.toEntity(initializer);
        entity.setName(I18nHolder.getAccessor().getMessage(entity.getPath(), entity.getName()));
        return entity;
    }

    @Override
    public void execute(ApplicationArguments args) {
        // 从运行参数或环境变量中判断是否包含指定参数
        if (Finders.hasTrue(args, SKIP_EXECUTE_ARGS_NAME)) {
            log.warn(I18nHolder.getAccessor().getMessage(
                    "InitTaskService.execute.skipMessage", new Object[]{SKIP_EXECUTE_ARGS_NAME},
                    Strings.lenientFormat("发现 --%s=true 将跳过自动执行", SKIP_EXECUTE_ARGS_NAME)));
            return;
        }

        if (CollectionUtils.isEmpty(initializers)) {
            log.warn(I18nHolder.getAccessor().getMessage("InitTaskService.execute.ignored",
                    "未发现任何初始化器的实现，已忽略自动执行"));
            return;
        }

        initializers.stream()
                .filter(it -> TaskType.SYSTEM.equals(it.getType()))
                .forEach(this::attemptExecute);
    }

    private void attemptExecute(Initializer initializer) {
        String path = initializer.getPath();
        InitTask task = repository.findByPath(path);
        if (task == null || !task.isExecutable()) {
            log.warn(I18nHolder.getAccessor().getMessage(
                    "InitTaskService.execute.skipped", new Object[]{task},
                    String.format("初始化任务 %s，无需执行", task)));
            return;
        }

        String name = task.getName();
        log.info(I18nHolder.getAccessor().getMessage(
                "InitTaskService.execute.started", new Object[]{name, path},
                Strings.lenientFormat("准备执行初始化任务：{}--[{}]", name, path)));

        task.setStatus(TaskStatus.STARTED);
        InitTaskLog taskLog = InitTaskLog.of(task);
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            initializer.run();
            // 如果是每次运行模式，那么设置初始化任务为默认状态，否则设置为已完成状态（表示只执行一次）
            TaskStatus status = TaskMode.EACH.equals(initializer.getMode())
                    ? TaskStatus.DEFAULT : TaskStatus.COMPLETED;
            task.setStatus(status);

            Stopwatch stop = stopwatch.stop();
            String successMessage = I18nHolder.getAccessor().getMessage(
                    "InitTaskService.execute.success", new Object[]{name, stop},
                    Strings.lenientFormat("初始化任务 %s 执行成功，用时：%s", name, stop));
            taskLog.setMessage(successMessage);
            log.info(successMessage);
        } catch (InitializationException e) {
            TaskStatus status = TaskMode.EACH.equals(initializer.getMode())
                    ? TaskStatus.DEFAULT : TaskStatus.FAILED;
            task.setStatus(status);
            String cause = Validations.findMessage(e);
            String trace = Exceptions.ofTrace(e);
            String failedMessage = I18nHolder.getAccessor().getMessage(
                    "InitTaskService.execute.failure", new Object[]{name, cause},
                    Strings.lenientFormat("初始化任务 %s 执行失败，原因：%s", name, cause));
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
