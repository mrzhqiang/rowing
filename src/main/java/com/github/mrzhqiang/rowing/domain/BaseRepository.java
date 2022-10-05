package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Authorizations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Nonnull;

/**
 * 基础仓库。
 * <p>
 * 设定如果继承此基础仓库，则必须使用 {@link BaseEntity} 的扩展实体，以规范主键和版本字段。
 * <p>
 * 这样做可以方便未来对继承的接口进行改进，比如增加 {@link JpaSpecificationExecutor} 接口，或降级为 {@link PagingAndSortingRepository} 分页排序仓库。
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories">Spring Data JPA 仓库</a>
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#core.web.basic.paging-and-sorting">内置支持 web 请求参数</a>
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/rest/docs/current/reference/html/#repository-resources">使用 REST 框架不再手写 CURD 接口</a>
 * <p>
 * 注意：基础仓库的所有方法默认需要 {@link Authority#ROLE_USER} 权限，这里声明所有删除方法需要 {@link Authority#ROLE_ADMIN} 权限。
 *
 * @param <E> 实体类型。
 */
@PreAuthorize(Authorizations.HAS_ROLE_USER)
@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, Long> {

    @PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
    @Override
    void deleteAllInBatch(@Nonnull Iterable<E> entities);

    @PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
    @Override
    void deleteAllByIdInBatch(@Nonnull Iterable<Long> longs);

    @PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
    @Override
    void deleteAllInBatch();

    @PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
    @Override
    void deleteById(@Nonnull Long aLong);

    @PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
    @Override
    void delete(@Nonnull E entity);

    @PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
    @Override
    void deleteAllById(@Nonnull Iterable<? extends Long> longs);

    @PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
    @Override
    void deleteAll(@Nonnull Iterable<? extends E> entities);

    @PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
    @Override
    void deleteAll();
}
