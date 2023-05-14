package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

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

}
