package com.github.mrzhqiang.rowing.core.account;

import com.github.mrzhqiang.rowing.core.domain.BaseRepository;
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
     * 通过用户名找到可能存在的账户。
     *
     * 由于登录时需要调用此方法，因此授权给匿名用户调用，但不作为公开的 rest api 接口。
     *
     * @param username 用户名。
     * @return 可选的账户。
     */
    @PreAuthorize(Authorizations.HAS_ROLE_ANONYMOUS)
    @RestResource(exported = false)
    Optional<Account> findByUsername(String username);
}
