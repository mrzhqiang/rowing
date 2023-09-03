package com.github.mrzhqiang.rowing.menu;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 菜单映射器。
 * <p>
 */
@Mapper(componentModel = "spring", imports = {Menus.class})
public interface MenuMapper {

    @Mapping(target = "title", source = "route.meta.title")
    @Mapping(target = "noCache", source = "route.meta.noCache")
    @Mapping(target = "icon", source = "route.meta.icon")
    @Mapping(target = "breadcrumb", source = "route.meta.breadcrumb")
    @Mapping(target = "affix", source = "route.meta.affix")
    @Mapping(target = "activeMenu", source = "route.meta.activeMenu")
    @Mapping(target = "roleList", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "resourceList", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "ordered", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fullPath", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    Menu toEntity(MenuRoute route);

    @Mapping(target = "meta", ignore = true)
    @Mapping(target = "meta.roles", expression = "java( Menus.convertRoles(menu.getRoleList()) )")
    @Mapping(target = "meta.title", source = "title")
    @Mapping(target = "meta.noCache", source = "noCache")
    @Mapping(target = "meta.icon", source = "icon")
    @Mapping(target = "meta.breadcrumb", source = "breadcrumb")
    @Mapping(target = "meta.affix", source = "affix")
    @Mapping(target = "meta.activeMenu", source = "activeMenu")
    MenuRoute toRoute(Menu menu);

}
