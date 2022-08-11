package com.github.mrzhqiang.rowing.system.init;

import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.system.AutoInitializer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = TaskStatus.class)
public interface SysInitMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "type", expression = "java( SysInit.Type.REQUIRED )")
    @Mapping(target = "name", source = "initializer.name")
    @Mapping(target = "status", expression = "java( com.github.mrzhqiang.rowing.domain.TaskStatus.DEFAULT )")
    @Mapping(target = "ordered", source = "initializer.order")
    SysInit toEntity(AutoInitializer initializer);
}
