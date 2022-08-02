package com.github.mrzhqiang.rowing.api.system.init;

import com.github.mrzhqiang.rowing.api.system.AutoInitializer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysInitMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "type", expression = "java( SysInit.Type.REQUIRED )")
    @Mapping(target = "name", source = "initializer.name")
    @Mapping(target = "status", expression = "java( SysInit.Status.DEFAULT )")
    @Mapping(target = "ordered", source = "initializer.order")
    SysInit toEntity(AutoInitializer initializer);
}
