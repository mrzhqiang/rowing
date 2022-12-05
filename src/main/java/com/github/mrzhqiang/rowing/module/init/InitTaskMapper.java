package com.github.mrzhqiang.rowing.module.init;

import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskType;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.module.AutoInitializer;
import com.github.mrzhqiang.rowing.module.Initializer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 系统初始化映射器。
 * <p>
 * 主要针对不同的数据来源，可以转为系统初始化实体。
 */
@Mapper(componentModel = "spring", imports = {TaskType.class, TaskStatus.class, Logic.class})
public interface InitTaskMapper {

    @Mapping(target = "i18nName", ignore = true)
    @Mapping(target = "discard", expression = "java( Logic.NO )")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "errorTrace", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "type", expression = "java( TaskType.OPTIONAL )")
    @Mapping(target = "name", source = "initializer.name")
    @Mapping(target = "path", source = "initializer.path")
    @Mapping(target = "status", expression = "java( TaskStatus.DEFAULT )")
    @Mapping(target = "ordered", source = "initializer.order")
    InitTask toEntity(Initializer initializer);
}
