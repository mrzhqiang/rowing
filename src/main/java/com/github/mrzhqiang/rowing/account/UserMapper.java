package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.system.data.DataDictItemMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = {DataDictItemMapper.class})
public interface UserMapper {

    @Mapping(target = "age", source = "entity.birthday")
    UserData toData(User entity);

    /**
     * 转换当前日期为年龄。
     *
     * @return 年龄。即从生日那一天开始，距离今天共有多少年。
     */
    static Integer convertAge(LocalDate birthday) {
        return Optional.ofNullable(birthday)
                .map(it -> ChronoUnit.YEARS.between(it, LocalDate.now()))
                .map(Long::intValue)
                .orElse(0);
    }
}
