package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.account.Account;

/**
 * 角色服务。
 * <p>
 */
public interface RoleService {

    /**
     * 角色初始化。
     */
    void init();

    /**
     * 为角色绑定管理员账户。
     *
     * @param admin 管理员账户。
     */
    void bindingAccount(Account admin);

}
