package com.github.mrzhqiang.rowing.modules.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.domain.DictType;
import com.github.mrzhqiang.rowing.util.Authorizations;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * 数据字典组仓库。
 */
@RepositoryRestResource(path = "dict", collectionResourceRel = "dict")
public interface DictGroupRepository extends BaseRepository<DictGroup> {

    @RestResource(exported = false)
    void deleteAllByType(DictType type);

    @RestResource(exported = false)
    boolean existsByCode(String code);

    /**
     * 根据代码找到可能存在的字典组。
     *
     * @param code 字典组代码。
     * @return 可选的字典组。
     */
    Optional<DictGroup> findByCode(String code);

}
