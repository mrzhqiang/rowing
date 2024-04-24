package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.account.Account;

/**
 * 用户服务。
 */
public interface UserService {

    /**
     * 通过用户名，找到用户信息数据。
     *
     * @param username 用户名。
     * @return 用户信息数据。
     */
    UserInfoData findByUsername(String username);

    /**
     * 绑定账户。
     * <p>
     * 通常在账户创建完成后调用一次，不推荐重复调用，可能导致账户绑定的用户信息被覆盖。
     *
     * @param account 账户实体。
     */
    void binding(Account account);

}
