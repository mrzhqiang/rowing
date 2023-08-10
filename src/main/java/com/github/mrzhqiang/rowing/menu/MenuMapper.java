package com.github.mrzhqiang.rowing.menu;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 菜单映射器。
 * <p>
 */
@Mapper(componentModel = "spring")
public interface MenuMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "roleList", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "resourceList", ignore = true)
    Menu toEntity(MenuData data);
}
