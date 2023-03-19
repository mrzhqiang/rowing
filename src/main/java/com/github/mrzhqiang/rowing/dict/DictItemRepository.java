package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * 字典项仓库。
 */
@RepositoryRestResource(exported = false)
public interface DictItemRepository extends BaseRepository<DictItem> {

    Optional<DictItem> findByGroup_CodeAndValue(String code, String value);
}
