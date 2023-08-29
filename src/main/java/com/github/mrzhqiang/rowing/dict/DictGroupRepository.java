package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * 字典组仓库。
 */
@RepositoryRestResource(path = "dict-group", collectionResourceRel = "dict-group")
public interface DictGroupRepository extends BaseRepository<DictGroup> {

    Optional<DictGroup> findByCode(String code);

}
