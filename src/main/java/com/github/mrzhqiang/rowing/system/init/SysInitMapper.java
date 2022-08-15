package com.github.mrzhqiang.rowing.system.init;

import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.system.AutoInitializer;
import com.github.mrzhqiang.rowing.util.DateTimes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {TaskStatus.class, DateTimes.class})
public interface SysInitMapper {

    @Mapping(target = "type", source = "entity.type.label")
    @Mapping(target = "status", source = "entity.status.label")
    @Mapping(target = "startTime", expression = "java( DateTimes.localFormat(entity.getStartTime()) )")
    @Mapping(target = "endTime", expression = "java( DateTimes.localFormat(entity.getEndTime()) )")
    SysInitData toData(SysInit entity);

    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "errorStack", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "type", expression = "java( SysInit.Type.REQUIRED )")
    @Mapping(target = "name", source = "initializer.name")
    @Mapping(target = "status", expression = "java( TaskStatus.DEFAULT )")
    @Mapping(target = "ordered", source = "initializer.order")
    SysInit toEntity(AutoInitializer initializer);
}
