package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Authorizes;
import static com.github.mrzhqiang.rowing.util.Authorizes.HAS_AUTHORITY_ADMIN;
import static com.github.mrzhqiang.rowing.util.Authorizes.HAS_AUTHORITY_USER;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
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
 * 设定如果继承此基础仓库，则必须使用 {@link BaseEntity} 的扩展实体，以规范主键和版本字段。
 * <p>
 * 这样做可以方便未来对继承的接口进行改进，比如增加 {@link QuerydslPredicateExecutor} 接口。
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories">spring data jpa repositories</a>
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#core.web.basic.paging-and-sorting">spring data jpa paging and sorting</a>
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/rest/docs/current/reference/html/#repository-resources">spring data rest repository-resources</a>
 * <p>
 * 注意：这里声明所有删除方法需要 {@link Authorizes#HAS_AUTHORITY_ADMIN} 权限。
 *
 * @see org.springframework.data.jpa.repository.JpaRepository 基于 JPA 规范的仓库接口。
 * 实际上只有 {@link PagingAndSortingRepository} 和 {@link CrudRepository} 接口中的方法会被 REST 框架通过反射进行调用，
 * 对于 {@link QueryByExampleExecutor} 接口中的方法，不会被 REST 框架调用，只能通过自定义 Controller 及 Service 来手动调用。
 *
 * @param <E> 实体类型。
 */
@PreAuthorize(HAS_AUTHORITY_USER)
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

    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    @Override
    void deleteById(@Nonnull Long id);

    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    @Override
    void delete(@Nonnull E entity);

}
