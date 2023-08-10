package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.account.Account;

/**
 * 用户服务。
 */
public interface UserService {

    /**
     * 通过所属账户，找到用户信息。
     *
     * @param username 用户名。
     * @return 用户信息数据。
     */
    UserInfoData findByUsername(String username);

    void createForAdmin(Account admin);
}
