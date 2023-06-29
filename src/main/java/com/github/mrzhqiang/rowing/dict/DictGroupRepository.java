package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.aop.SystemAuth;
import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * 字典组仓库。
 */
@RepositoryRestResource(path = "dict", collectionResourceRel = "dict")
public interface DictGroupRepository extends BaseRepository<DictGroup> {

    @RestResource(exported = false)
    boolean existsByCode(String code);

    /**
     * 根据代码找到可能存在的字典组。
     *
     * @param code 字典组代码。
     * @return 可选的字典组。
     */
    Optional<DictGroup> findByCode(String code);

    @Nonnull
    @SystemAuth
    @Override
    List<DictGroup> findAll();

    @Nonnull
    @SystemAuth
    @Override
    List<DictGroup> findAll(@Nonnull Sort sort);

    @Nonnull
    @SystemAuth
    @Override
    <S extends DictGroup> List<S> findAll(@Nonnull Example<S> example);

    @Nonnull
    @SystemAuth
    @Override
    <S extends DictGroup> List<S> findAll(@Nonnull Example<S> example, @Nonnull Sort sort);

    @Nonnull
    @SystemAuth
    @Override
    Page<DictGroup> findAll(@Nonnull Pageable pageable);

    @Nonnull
    @SystemAuth
    @Override
    <S extends DictGroup> Page<S> findAll(@Nonnull Example<S> example, @Nonnull Pageable pageable);
}
