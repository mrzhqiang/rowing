package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.system.data.DataDictItemData;
import lombok.Data;

@Data
public class UserData {

    private String nickname;
    private DataDictItemData gender;
    private Integer age;
}
