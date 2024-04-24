package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.domain.TaskType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.ClassUtils;

/**
 * 初始化映射器。
 */
@Mapper(componentModel = "spring", imports = {
        Logic.class,
        TaskStatus.class,
        TaskType.class,
        ClassUtils.class,
        InitializationOrderRegistration.class})
public interface InitTaskMapper {

    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "discard", expression = "java(Logic.NO)")
    @Mapping(target = "status", expression = "java(TaskStatus.DEFAULT)")
    @Mapping(target = "type", source = "initializer.type")
    @Mapping(target = "name", expression = "java(ClassUtils.getShortName(initializer.getClass()))")
    @Mapping(target = "path", source = "initializer.path")
    @Mapping(target = "ordered", expression = "java(InitializationOrderRegistration.find(initializer))")
    InitTask toEntity(Initializer initializer);

}
