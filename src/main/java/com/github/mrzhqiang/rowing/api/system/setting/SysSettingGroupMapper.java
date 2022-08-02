package com.github.mrzhqiang.rowing.api.system.setting;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SysSettingGroupMapper {

    SysSettingGroupData toData(SysSettingGroup entity);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    SysSettingGroup toEntity(SysSettingGroupForm form);
}
