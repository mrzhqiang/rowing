package com.github.mrzhqiang.rowing.init;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 初始化映射器。
 */
@Mapper(componentModel = "spring")
public interface InitTaskLogMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "task", source = "task")
    InitTaskLog toEntity(InitTask task);

}
