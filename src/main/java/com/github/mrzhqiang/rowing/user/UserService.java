package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.account.Account;

/**
 * 用户服务。
 */
public interface UserService {

    /**
     * 通过所属账号，找到用户信息。
     *
     * @param owner 所属账号。
     * @return 用户信息数据。
     */
    UserInfoData findByOwner(Account owner);
}
