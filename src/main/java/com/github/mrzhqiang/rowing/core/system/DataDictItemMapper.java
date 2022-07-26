package com.github.mrzhqiang.rowing.core.system;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DataDictItemMapper {

    DataDictItemData toData(DataDictItem entity);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    DataDictItem toEntity(DataDictItemForm form);
}
