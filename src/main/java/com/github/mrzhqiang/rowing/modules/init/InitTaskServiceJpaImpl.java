package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 初始化任务服务的 JPA 实现。
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class InitTaskServiceJpaImpl implements InitTaskService {

    private final InitTaskMapper mapper;
    private final InitTaskRepository repository;
    private final List<Initializer> initializers;

    public InitTaskServiceJpaImpl(InitTaskMapper mapper,
                                  InitTaskRepository repository,
                                  List<Initializer> initializers) {
        this.mapper = mapper;
        this.repository = repository;
        this.initializers = initializers;
    }

    @Override
    public InitTaskSyncData checkSyncData() {
        // 代码实现的初始化运行器不存在，需要将所有未废弃的初始化任务进行废弃处理
        if (CollectionUtils.isEmpty(initializers)) {
            List<InitTask> discards = repository.findAllByDiscard(Logic.NO).stream()
                    .peek(it -> it.setDiscard(Logic.YES))
                    .map(repository::save)
                    .collect(Collectors.toList());
            return InitTaskSyncData.of(Collections.emptyList(), discards);
        }

        // 找到未被记录的初始化任务实现，进行新增
        List<InitTask> addedList = initializers.stream()
                .filter(it -> !repository.existsByPath(it.getPath()))
                .map(this::convertEntity)
                .map(repository::save)
                .collect(Collectors.toList());

        // 存在已删除的初始化任务实现，进行废弃
        List<String> paths = initializers.stream()
                .map(Initializer::getPath)
                .collect(Collectors.toList());
        List<InitTask> discardList = Collections.emptyList();
        if (!CollectionUtils.isEmpty(paths)) {
            discardList = repository.findAllByPathNotIn(paths).stream()
                    .peek(it -> it.setDiscard(Logic.YES))
                    .map(repository::save)
                    .collect(Collectors.toList());
        }

        return InitTaskSyncData.of(addedList, discardList);
    }

    private InitTask convertEntity(Initializer initializer) {
        InitTask initTask = mapper.toEntity(initializer);
        // 如果是自动初始化实现，那么属于系统类型的初始化任务
        if (initializer instanceof AutoInitializer) {
            initTask.setType(TaskType.SYSTEM);
            initTask.setOrdered(((AutoInitializer) initializer).getOrder());
        }
        return initTask;
    }
}
