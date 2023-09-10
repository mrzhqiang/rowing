package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import static com.github.mrzhqiang.rowing.util.Authorizes.HAS_AUTHORITY_ADMIN;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * 账户仓库。
 */
@PreAuthorize(HAS_AUTHORITY_ADMIN)
@RepositoryRestResource(path = "account", excerptProjection = AccountExcerpt.class)
public interface AccountRepository extends BaseRepository<Account> {

    /**
     * 检测指定用户名是否存在。
     *
     * @param username 用户名。
     * @return 返回 true 表示用户名已存在；否则表示不存在。
     */
    @RestResource(exported = false)
    boolean existsByUsername(String username);

    /**
     * 通过用户名找到账户。
     * <p>
     * 由于认证需要调用此方法，因此公开授权，但不暴露为 rest api 接口。
     *
     * @param username 用户名。
     * @return 可选的账户。
     */
    @RestResource(exported = false)
    Optional<Account> findByUsername(String username);

    @RestResource(path = "page", rel = "page")
    Page<Account> findAllByUsernameContaining(String username, Pageable pageable);

}
