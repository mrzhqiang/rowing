package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.helper.Exceptions;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.domain.TaskType;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 初始化任务服务的 JPA 实现。
 */
@Slf4j
@Service
public class InitTaskServiceJpaImpl implements InitTaskService {

    private final InitTaskMapper mapper;
    private final InitTaskRepository repository;
    private final InitTaskLogRepository logRepository;
    private final MessageSourceAccessor sourceAccessor;
    private final List<Initializer> initializers;

    public InitTaskServiceJpaImpl(InitTaskMapper mapper,
                                  InitTaskRepository repository,
                                  InitTaskLogRepository logRepository,
                                  MessageSource messageSource,
                                  List<Initializer> initializers) {
        this.mapper = mapper;
        this.repository = repository;
        this.logRepository = logRepository;
        this.sourceAccessor = new MessageSourceAccessor(messageSource);
        this.initializers = initializers;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void syncData(ApplicationArguments args) {
        // 从运行参数或环境变量中判断是否包含指定参数
        if (args.containsOption(SKIP_SYNC_ARGS_NAME)) {
            // 解析选项参数，如果存在 true 表示跳过检测
            List<String> optionValues = args.getOptionValues(SKIP_SYNC_ARGS_NAME);
            if (!CollectionUtils.isEmpty(optionValues)
                    && optionValues.contains(Boolean.TRUE.toString())) {
                String skipMessage = sourceAccessor.getMessage("InitTaskService.syncData.skipMessage",
                        new Object[]{SKIP_SYNC_ARGS_NAME},
                        Strings.lenientFormat("发现 --%s=true 将跳过同步数据", SKIP_SYNC_ARGS_NAME));
                log.info(skipMessage);
                return;
            }
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
        List<InitTask> discardList = repository.findAllByDiscardAndPathNotIn(Logic.NO, paths).stream()
                .peek(it -> it.setDiscard(Logic.YES))
                .map(repository::save)
                .collect(Collectors.toList());

        // 打印简要的初始化报告
        String reportMessage = sourceAccessor.getMessage("InitTaskService.syncData.simpleReport",
                new Object[]{addedList.size(), discardList.size()},
                Strings.lenientFormat("新增 %s 个初始化任务，废弃 %s 初始化任务",
                        addedList.size(), discardList.size()));
        log.info(reportMessage);

        // 如果开启调试日志，则打印初始化报告详情
        if (log.isDebugEnabled()) {
            if (!addedList.isEmpty()) {
                String addMessage = sourceAccessor.getMessage("InitTaskService.syncData.addReport",
                        new Object[]{addedList},
                        Strings.lenientFormat("新增初始化任务详情：%s", addedList));
                log.debug(addMessage);
            }
            if (!discardList.isEmpty()) {
                String discardMessage = sourceAccessor.getMessage("InitTaskService.syncData.discardReport",
                        new Object[]{discardList},
                        Strings.lenientFormat("废弃初始化任务详情：%s", discardList));
                log.debug(discardMessage);
            }
        }

    }

    private InitTask convertEntity(Initializer initializer) {
        InitTask entity = mapper.toEntity(initializer);
        // 根据 ServletRequest 的 getLocale 方法指示：
        // 当请求头中包含 Accept-Language 值时，返回指定的语言代码，否则返回系统默认语言代码
        // Spring 包装的 SavedRequest 会将返回的语言代码保存到本地线程中
        // 当通过下面的方法获取国际化消息内容时，自动从本地线程中取得请求对应的语言代码，从而获得对应语言的国际化内容
        entity.setName(sourceAccessor.getMessage(entity.getPath(), entity.getName()));
        // 如果是自动执行初始化，那么属于系统类型的初始化任务
        if (initializer.isAutoExecute()) {
            entity.setType(TaskType.SYSTEM);
        }
        return entity;
    }

    @Override
    public void execute(ApplicationArguments args) {
        // 从运行参数或环境变量中判断是否包含指定参数
        if (args.containsOption(SKIP_EXECUTE_ARGS_NAME)) {
            // 解析选项参数，如果存在 true 表示跳过执行
            List<String> optionValues = args.getOptionValues(SKIP_EXECUTE_ARGS_NAME);
            if (!CollectionUtils.isEmpty(optionValues)
                    && optionValues.contains(Boolean.TRUE.toString())) {
                String skipMessage = sourceAccessor.getMessage("InitTaskService.autoExecute.skipMessage",
                        new Object[]{SKIP_EXECUTE_ARGS_NAME},
                        Strings.lenientFormat("发现 --%s=true 将跳过自动执行", SKIP_EXECUTE_ARGS_NAME));
                log.info(skipMessage);
                return;
            }
        }

        if (CollectionUtils.isEmpty(initializers)) {
            String ignoredMessage = sourceAccessor.getMessage("InitTaskService.autoExecute.ignored",
                    "未发现任何初始化器的实现，已跳过自动执行");
            log.info(ignoredMessage);
            return;
        }

        initializers.stream()
                .filter(Initializer::isAutoExecute)
                .forEach(this::attemptExecute);

    }

    private void attemptExecute(Initializer initializer) {
        InitTask task = repository.findByPath(initializer.getPath());
        if (task == null || !task.isExecutable()) {
            log.warn(sourceAccessor.getMessage("InitTaskService.autoExecute.skipped",
                    new Object[]{task},
                    String.format("初始化任务 %s，无需执行", task)));
            return;
        }

        String name = task.getName();
        String path = task.getPath();
        log.info(sourceAccessor.getMessage("InitTaskService.autoExecute.started",
                new Object[]{name, path},
                Strings.lenientFormat("准备执行初始化任务：{}--[{}]", name, path)));

        task.setStatus(TaskStatus.STARTED);
        InitTaskLog taskLog = InitTaskLog.of(task);
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            initializer.execute();
            // 如果支持重复执行，那么设置为默认状态，否则设置为已完成状态
            TaskStatus taskStatus = initializer.isSupportRepeat() ? TaskStatus.DEFAULT : TaskStatus.COMPLETED;
            task.setStatus(taskStatus);

            Stopwatch stop = stopwatch.stop();
            String successMessage = sourceAccessor.getMessage("InitTaskService.autoExecute.success",
                    new Object[]{name, stop},
                    Strings.lenientFormat("初始化任务 %s 执行成功，用时：%s", name, stop));
            taskLog.setMessage(successMessage);
            log.info(successMessage);
        } catch (InitializationException e) {
            task.setStatus(TaskStatus.FAILED);
            String cause = Exceptions.ofMessage(e);
            String trace = Exceptions.ofTrace(e);
            String failedMessage = sourceAccessor.getMessage("InitTaskService.autoExecute.failure",
                    new Object[]{name, cause},
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
