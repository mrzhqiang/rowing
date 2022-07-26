package com.github.mrzhqiang.rowing.core.account;

import lombok.Data;

/**
 * 账号数据。
 * <p>
 * 通常用于页面展示，需要尽可能避免深度解析实体的关系。
 */
@Data
public class AccountData {

    /**
     * 用户名。
     */
    private String username;
    /**
     * 用户数字编号。
     */
    private String uid;
}
