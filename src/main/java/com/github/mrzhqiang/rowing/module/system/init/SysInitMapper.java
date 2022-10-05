package com.github.mrzhqiang.rowing.module.system.init;

import com.github.mrzhqiang.rowing.module.domain.SysInitType;
import com.github.mrzhqiang.rowing.module.domain.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 系统初始化映射器。
 * <p>
 * 主要针对不同的数据来源，可以转为系统初始化实体。
 */
@Mapper(componentModel = "spring", imports = {SysInitType.class, TaskStatus.class})
public interface SysInitMapper {

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "errorTrace", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "type", expression = "java( SysInitType.REQUIRED )")
    @Mapping(target = "name", source = "initializer.name")
    @Mapping(target = "path", source = "initializer.path")
    @Mapping(target = "status", expression = "java( TaskStatus.DEFAULT )")
    @Mapping(target = "ordered", source = "initializer.order")
    SysInit toEntity(AutoInitializer initializer);
}
