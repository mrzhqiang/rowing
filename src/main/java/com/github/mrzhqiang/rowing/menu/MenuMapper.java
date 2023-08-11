package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.role.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单映射器。
 * <p>
 */
@Mapper(componentModel = "spring")
public interface MenuMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "meta.roleList", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "resourceList", ignore = true)
    Menu toEntity(MenuData data);

    @Mapping(target = "meta.roles", source = "meta.roleList")
    MenuData toData(Menu menu);

    default List<String> convertRoleList(List<Role> roleList) {
        return roleList.stream().map(Role::getCode).collect(Collectors.toList());
    }

    MenuResourceData toData(MenuResource menuResource);

}
