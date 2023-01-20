package com.github.mrzhqiang.rowing.modules.account;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.util.Authorizations;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * 账号仓库。
 */
@RepositoryRestResource(path = "account", collectionResourceRel = "account")
@PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
public interface AccountRepository extends BaseRepository<Account> {

    /**
     * 检测指定用户名是否存在。
     *
     * @param username 用户名。
     * @return 返回 true 表示用户名已存在；否则表示不存在。
     */
    @PreAuthorize(Authorizations.HAS_ROLE_ANONYMOUS)
    @RestResource(exported = false)
    boolean existsByUsername(String username);

    /**
     * 通过用户名找到可能存在的账户。
     * <p>
     * 由于登录时需要调用此方法，因此授权给匿名用户调用，但不作为公开的 rest api 接口。
     *
     * @param username 用户名。
     * @return 可选的账户。
     */
    @PreAuthorize(Authorizations.HAS_ROLE_ANONYMOUS)
    @RestResource(exported = false)
    Optional<Account> findByUsername(String username);
}
