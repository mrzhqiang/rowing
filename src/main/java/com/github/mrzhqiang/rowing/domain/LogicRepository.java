package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Authorizes;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

/**
 * 逻辑仓库。
 * <p>
 * 设定如果继承此基础仓库，则必须使用 {@link LogicEntity} 的扩展实体，以规范逻辑删除字段。
 *
 * @param <E> 实体类型。
 */
@NoRepositoryBean
public interface LogicRepository<E extends LogicEntity> extends BaseRepository<E> {

    /**
     * 根据主键逻辑删除实体。
     *
     * @param id 主键 id。
     */
    @PreAuthorize(Authorizes.HAS_ROLE_ADMIN)
    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.deleted = 'YES' where e.id = ?1")
    void logicDeleteById(@Nonnull Long id);

    /**
     * 逻辑删除所有实体。
     */
    @PreAuthorize(Authorizes.HAS_ROLE_ADMIN)
    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.deleted = 'YES'")
    void logicDeleteAll();
}
