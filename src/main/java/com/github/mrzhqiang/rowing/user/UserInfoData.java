package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.menu.MenuData;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 用户信息数据。
 */
@Data
public class UserInfoData {

    /**
     * 昵称。
     */
    private String nickname;
    /**
     * 头像地址。
     */
    private String avatar;
    /**
     * 性别。
     */
    private String gender;
    /**
     * 生日。
     */
    private LocalDate birthday;
    /**
     * 年龄。
     */
    private Integer age;
    /**
     * 电子邮箱。
     */
    private String email;
    /**
     * 电话号码。
     */
    private String phoneNumber;
    /**
     * 简介。
     */
    private String introduction;
    /**
     * 角色代码列表。
     */
    private List<String> roles;
    /**
     * 用户菜单列表。
     */
    private List<MenuData> menus;

}
