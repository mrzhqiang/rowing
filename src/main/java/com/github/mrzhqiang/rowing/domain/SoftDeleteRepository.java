package com.github.mrzhqiang.rowing.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * 软删除仓库。
 * <p>
 * 设定如果继承此基础仓库，则必须使用 {@link SoftDeleteEntity} 的扩展实体，以规范软删除字段。
 * <p>
 * 注意：这个仓库没有重写基础仓库的两个删除方法，因此这两个删除方法依旧是物理删除。
 *
 * @param <E> 实体类型。
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface SoftDeleteRepository<E extends SoftDeleteEntity> extends BaseRepository<E> {

    /**
     * 通过主键 ID 找到真实实体。
     * <p>
     * 所谓真实实体就是忽略软删除字段查询得到的实体，即不论是否软删除都允许查询得到。
     *
     * @param id 主键 ID 值。
     * @return 可选的实体。
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Query("select e from #{#entityName} e where e.id = ?1")
    Optional<E> findRealById(@Nonnull Long id);

    /**
     * 查询分页的真实实体。
     *
     * @param pageable 分页参数。
     * @return 分页数据。
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Query(value = "select e from #{#entityName} e", countQuery = "select count(e) from #{#entityName} e")
    Page<E> findRealAll(@Nonnull Pageable pageable);

    /**
     * 查询排序的真实实体。
     *
     * @param sort 排序参数。
     * @return 数据列表。
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Query("select e from #{#entityName} e")
    List<E> findRealAll(@Nonnull Sort sort);

    /**
     * 查询所有真实实体。
     *
     * @return 数据列表。
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Query("select e from #{#entityName} e")
    List<E> findRealAll();

    /**
     * 通过主键 ID 软删除指定实体。
     *
     * @param id 主键 id。
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.deleted = 'YES' where e.id = ?1 and e.deleted = 'NO'")
    void logicDeleteById(@Nonnull Long id);

    /**
     * 软删除所有实体。
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.deleted = 'YES' where e.deleted = 'NO'")
    void logicDeleteAll();

}
