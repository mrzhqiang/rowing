package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.account.Account;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * 角色服务。
 */
public interface RoleService {

    /**
     * 角色数据初始化。
     * <p>
     * 通常在系统第一次启动时，初始化一次；或者在系统需要重置时调用。
     */
    void init();

    /**
     * 通过账户找到对应的授权列表。
     *
     * @param account 账户。
     * @return 授权列表。
     */
    List<GrantedAuthority> findAuthoritiesBy(Account account);

    /**
     * 为账户绑定角色。
     * <p>
     * 通常在账户创建完成之后调用一次，不推荐重复调用，可能导致账户权限信息被覆盖。
     *
     * @param account 账户。
     */
    void binding(Account account);

    /**
     * 添加账户。
     * <p>
     * 即将账户添加到角色的账户列表中，表示给账户授权此角色。
     *
     * @param roleCode 角色代码。
     * @param account  账户。
     */
    void addAccount(String roleCode, Account account);

    /**
     * 移除账户。
     * <p>
     * 即将账户从角色的账户列表中移除，表示为账户取消授权此角色。
     *
     * @param roleCode 角色代码。
     * @param account  账户。
     */
    void removeAccount(String roleCode, Account account);
}
