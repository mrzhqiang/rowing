package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.helper.time.Ages;
import com.github.mrzhqiang.rowing.role.Role;
import com.github.mrzhqiang.rowing.role.Roles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.authority.AuthorityUtils;

@Mapper(componentModel = "spring", imports = {Ages.class, Roles.class})
public interface UserMapper {

    @Mapping(target = "menus", ignore = true)
    @Mapping(target = "age", expression = "java( Ages.ofFullYear(user.getBirthday()) )")
    @Mapping(target = "roles", expression = "java( Roles.findCodes(user.getOwner().getRoles()) )")
    UserInfoData toData(User user);

}
