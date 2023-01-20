package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.domain.TaskType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 初始化任务服务的 JPA 实现。
 */
@Service
@Transactional
public class InitTaskServiceJpaImpl implements InitTaskService {

    private final InitTaskMapper mapper;
    private final InitTaskRepository repository;
    private final List<Initializer> initializers;

    public InitTaskServiceJpaImpl(InitTaskMapper mapper,
                                  InitTaskRepository repository,
                                  List<Initializer> initializers) {
        this.initializers = initializers;
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public InitTaskSyncData syncData() {
        if (CollectionUtils.isEmpty(initializers)) {
            return InitTaskSyncData.empty();
        }

        // 将数据库中所有的初始化任务转为 Map 结构
        Map<String, InitTask> initTaskMap = repository.findAll().stream()
                .collect(Collectors.toMap(InitTask::getPath, Function.identity()));
        // 遍历初始化实现列表，检测是否有新增
        List<InitTask> addList = initializers.stream()
                .filter(it -> !initTaskMap.containsKey(it.getPath()))
                .map(mapper::toEntity)
                .collect(Collectors.toList());


        List<InitTask> addTasks = initializers.stream()
                .filter(it -> !repository.existsByPath(it.getPath()))
                .map(this::mapToEntity)
                .map(repository::save)
                .collect(Collectors.toList());
        return InitTaskSyncData.of(addTasks, Collections.emptyList());
    }

    private InitTask mapToEntity(Initializer initializer) {
        InitTask initTask = mapper.toEntity(initializer);
        // 如果是自动初始化实现，那么属于系统类型的初始化任务
        if (initializer instanceof AutoInitializer) {
            initTask.setType(TaskType.SYSTEM);
        }
        return initTask;
    }
}
