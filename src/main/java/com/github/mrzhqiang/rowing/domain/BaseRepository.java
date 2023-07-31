package com.github.mrzhqiang.rowing.domain;

import static com.github.mrzhqiang.rowing.util.Authorizes.HAS_ROLE_ADMIN;
import static com.github.mrzhqiang.rowing.util.Authorizes.HAS_ROLE_USER;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nonnull;

/**
 * 基础仓库。
 * <p>
 * 设定如果继承此基础仓库，则必须使用 {@link BaseEntity} 的扩展实体，以规范主键和版本字段。
 * <p>
 * 这样做可以方便未来对继承的接口进行改进，比如增加 {@link JpaSpecificationExecutor} 接口，或降级为 {@link PagingAndSortingRepository} 分页排序仓库。
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories">spring data jpa repositories</a>
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#core.web.basic.paging-and-sorting">spring data jpa paging and sorting</a>
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/rest/docs/current/reference/html/#repository-resources">spring data rest repository-resources</a>
 * <p>
 * 注意：这里声明所有删除方法需要 {@link Authority#ROLE_ADMIN} 权限。
 *
 * @param <E> 实体类型。
 */
@NoRepositoryBean
@PreAuthorize(HAS_ROLE_USER)
@Validated
public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, Long> {

    @PreAuthorize(HAS_ROLE_ADMIN)
    @Override
    void deleteAllInBatch(@Nonnull Iterable<E> entities);

    @PreAuthorize(HAS_ROLE_ADMIN)
    @Override
    void deleteAllByIdInBatch(@Nonnull Iterable<Long> longs);

    @PreAuthorize(HAS_ROLE_ADMIN)
    @Override
    void deleteAllInBatch();

    @PreAuthorize(HAS_ROLE_ADMIN)
    @Override
    void deleteById(@Nonnull Long aLong);

    @PreAuthorize(HAS_ROLE_ADMIN)
    @Override
    void delete(@Nonnull E entity);

    @PreAuthorize(HAS_ROLE_ADMIN)
    @Override
    void deleteAllById(@Nonnull Iterable<? extends Long> longs);

    @PreAuthorize(HAS_ROLE_ADMIN)
    @Override
    void deleteAll(@Nonnull Iterable<? extends E> entities);

    @PreAuthorize(HAS_ROLE_ADMIN)
    @Override
    void deleteAll();
}
