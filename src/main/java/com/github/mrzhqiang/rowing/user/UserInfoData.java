package com.github.mrzhqiang.rowing.user;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoData {

    private List<String> roles = Lists.newArrayList("admin");
    private String nickname;
    private String avatar;
    private String gender;
    private String introduction;
}
