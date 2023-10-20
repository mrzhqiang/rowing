package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.account.Account;

/**
 * 用户服务。
 */
public interface UserService {

    /**
     * 通过用户名，找到用户信息。
     *
     * @param username 用户名。
     * @return 用户信息数据。
     */
    UserInfoData findByUsername(String username);

    /**
     * 绑定账户。
     *
     * @param account 账户实体。
     */
    void binding(Account account);

}
