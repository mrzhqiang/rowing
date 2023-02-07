package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.domain.TaskType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 系统初始化映射器。
 * <p>
 * 主要针对不同的数据来源，可以转为系统初始化实体。
 */
@Mapper(componentModel = "spring", imports = {TaskType.class, TaskStatus.class, Logic.class})
public interface InitTaskMapper {

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "logHistories", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "discard", expression = "java(Logic.NO)")
    @Mapping(target = "ordered", expression = "java(Integer.MAX_VALUE)")
    @Mapping(target = "status", expression = "java(TaskStatus.DEFAULT)")
    @Mapping(target = "type", expression = "java(TaskType.OPTIONAL)")
    @Mapping(target = "name", source = "initializer.name")
    @Mapping(target = "path", source = "initializer.path")
    InitTask toEntity(Initializer initializer);
}
