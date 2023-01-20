package com.github.mrzhqiang.rowing.modules.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * 数据字典组仓库。
 */
@RepositoryRestResource(path = "/data-dict", collectionResourceRel = "/data-dict")
public interface DictGroupRepository extends BaseRepository<DictGroup> {

    /**
     * 根据代码判断是否存在字典组。
     */
    boolean existsByCode(String code);

    /**
     * 根据代码找到可能存在的字典组。
     *
     * @param code 字典组代码。
     * @return 可选的字典组。
     */
    Optional<DictGroup> findByCode(String code);

}
