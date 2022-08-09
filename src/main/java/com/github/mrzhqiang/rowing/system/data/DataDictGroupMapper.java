package com.github.mrzhqiang.rowing.system.data;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = DataDictItemMapper.class)
public interface DataDictGroupMapper {

    DataDictGroupData toData(DataDictGroup entity);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    DataDictGroup toEntity(DataDictGroupForm form);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    DataDictGroup update(DataDictGroupForm form, @MappingTarget DataDictGroup entity);
}
