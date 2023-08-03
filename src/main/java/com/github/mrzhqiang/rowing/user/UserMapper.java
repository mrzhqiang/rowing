package com.github.mrzhqiang.rowing.user;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "nickname", source = "nickname")
    @Mapping(target = "avatar", source = "avatar")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "introduction", source = "introduction")
    UserInfoData toData(User user);
}
