package com.github.mrzhqiang.rowing.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * 基础仓库。
 * <p>
 * 如果继承此基础仓库，必须使用 {@link BaseEntity 基础实体}，以规范主键和版本号字段。
 * <p>
 * 注意：这里声明所有的删除方法都需要 ROLE_MANAGER 权限，以避免非法的删除操作。
 * <p>
 * 权限判断方式，在 PreAuthorize 注解中添加以下字符串：
 * <p>
 * "hasAuthority('xxx')"
 * <p>
 * "hasRole('ROLE_xxx')" or "hasRole('xxx')"
 * <p>
 * "permitAll()"
 * <p>
 * "denyAll()"
 * <p>
 * "isAnonymous()"
 * <p>
 * "isAuthenticated()"
 * <p>
 * "isRememberMe()"
 * <p>
 * "isFullyAuthenticated()"
 * <p>
 * "hasPermission(#target, 'read')" or "hasPermission(#target, 'admin')"
 *
 * @param <E> 实体类型。
 * @see org.springframework.data.jpa.repository.JpaRepository 基于 JPA 规范的仓库接口。
 * 实际上只有 {@link PagingAndSortingRepository} 和 {@link CrudRepository} 接口中的方法会被 REST 框架通过反射进行调用，
 * 对于 {@link QueryByExampleExecutor} 接口中的方法，不会被 REST 框架调用。
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/2.7.14/reference/html/#repositories">Spring Data JPA Repositories</a>
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/2.7.14/reference/html/#core.web.basic.paging-and-sorting">Spring Data JPA Paging And Sorting</a>
 * @see <a href="https://docs.spring.io/spring-data/rest/docs/3.7.14/reference/html/#repository-resources">Spring Data REST Repository-Resources</a>
 */
@PreAuthorize("hasRole('USER')")
@Validated
@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, Long> {

    @Nonnull
    @Override
    <S extends E> S save(@Nonnull S entity);

    @Nonnull
    @Override
    Optional<E> findById(@Nonnull Long id);

    @Nonnull
    @Override
    Page<E> findAll(@Nonnull Pageable pageable);

    @Nonnull
    @Override
    List<E> findAll(@Nonnull Sort sort);

    @Nonnull
    @Override
    List<E> findAll();

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    void deleteById(@Nonnull Long id);

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    void delete(@Nonnull E entity);

}
