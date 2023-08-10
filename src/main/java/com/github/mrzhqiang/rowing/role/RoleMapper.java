package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.domain.AccountType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 角色映射器。
 * <p>
 * 用于数据与实体之间的映射，支持互相转换。
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "immutable", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "menuResourceList", ignore = true)
    @Mapping(target = "menuList", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "accountList", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "type.code")
    Role toEntity(AccountType type, String name);

}
