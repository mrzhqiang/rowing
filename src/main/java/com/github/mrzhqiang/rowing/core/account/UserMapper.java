package com.github.mrzhqiang.rowing.core.account;

import com.github.mrzhqiang.rowing.core.data.DataDictItemMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DataDictItemMapper.class})
public interface UserMapper {

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    User toEntity(UserForm form);
}
