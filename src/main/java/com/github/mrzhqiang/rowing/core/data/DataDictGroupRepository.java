package com.github.mrzhqiang.rowing.core.data;

import com.github.mrzhqiang.rowing.core.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * 数据字典组仓库。
 */
@RepositoryRestResource(path = "/data-dict", collectionResourceRel = "/data-dict")
public interface DataDictGroupRepository extends BaseRepository<DataDictGroup> {

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
    Optional<DataDictGroup> findByCode(String code);

}
