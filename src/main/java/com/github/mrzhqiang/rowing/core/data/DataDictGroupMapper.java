package com.github.mrzhqiang.rowing.core.data;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = DataDictItemMapper.class)
public interface DataDictGroupMapper {

}
