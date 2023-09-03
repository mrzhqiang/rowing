package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

/**
 * 字典组仓库。
 */
@RepositoryRestResource(path = "dict", collectionResourceRel = "dict", excerptProjection = DictExcerpt.class)
public interface DictGroupRepository extends BaseRepository<DictGroup> {

    @RestResource(path = "code", rel = "code")
    Optional<DictGroup> findByCode(String code);

}
