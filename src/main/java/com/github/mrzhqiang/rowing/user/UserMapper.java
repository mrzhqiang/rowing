package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.helper.time.Ages;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(imports = {Ages.class}, componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "menus", ignore = true)
    @Mapping(target = "age", expression = "java( Ages.ofFullYear(user.getBirthday()) )")
    UserInfoData toData(User user);

}
