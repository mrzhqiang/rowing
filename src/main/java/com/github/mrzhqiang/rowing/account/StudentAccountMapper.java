package com.github.mrzhqiang.rowing.account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 学生账户映射器。
 * <p>
 * 主要提供将各种数据来源映射为学生账户实体的功能。
 */
@Mapper(componentModel = "spring")
public interface StudentAccountMapper {

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "account", ignore = true)
    StudentAccount toEntity(StudentInfoForm form);
}
