package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.BaseRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('ADMIN')")
@RepositoryRestResource(path = "account", excerptProjection = AccountExcerpt.class)
public interface AccountRepository extends BaseRepository<Account> {

    /**
     * 通过用户名找到可能存在的账户。
     * <p>
     * 由于这个方法返回的是可选的账户实体，因此不对外暴露，避免获取到相关的隐私信息。
     *
     * @param username 用户名。
     * @return 可选的账户。
     * @see RestResource exported = false 表示不作为 RESTful 接口暴露。
     */
    @RestResource(exported = false)
    Optional<Account> findByUsername(String username);

    /**
     * 检测指定用户名是否存在。
     *
     * @param username 用户名。
     * @return 返回 true 表示用户名已存在；否则表示不存在。
     */
    @RestResource(exported = false)
    boolean existsByUsername(String username);

    /**
     * 模糊查询指定用户名的账户分页数据。
     * <p>
     * 注意 Containing 表示 %username% 的查询方式，如果传入 null 值，
     * 则表示查询 %% 数据，此时相当于放弃 username 字段的查询条件，将返回所有数据。
     * <p>
     * 在前端，请通过 {@code http://domain/account/page?page=0&size=10} 的方式调用暴露的 RestResource 接口。
     * <p>
     * 如果需要指定返回的内容，可以在以上查询参数中，添加 {@code projection=account-transfer} 参数来指定返回不同的投影数据。
     */
    @RestResource(path = "page", rel = "page")
    Page<Account> findAllByUsernameContaining(String username, Pageable pageable);

    /**
     * 列出所有账户接口。
     * <p>
     * 由于 findAll 方法会自动调用带分页参数的接口，无法做到返回所有账户数据，因此额外添加此接口。
     *
     * @return 账户列表。
     */
    @RestResource(path = "list", rel = "list")
    List<Account> findAllByUsernameNotNull();

}
