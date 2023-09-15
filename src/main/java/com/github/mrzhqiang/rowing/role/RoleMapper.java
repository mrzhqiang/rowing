package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.domain.AccountType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 角色映射器。
 * <p>
 * 用于数据与实体之间的映射，支持互相转换。
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "type.code")
    Role toEntity(AccountType type, String name);

}
