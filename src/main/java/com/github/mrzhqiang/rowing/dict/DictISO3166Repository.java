package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * 国家地区代码仓库。
 * <p>
 */
@RepositoryRestResource(path = "dict-iso-3166", collectionResourceRel = "dicts",
        excerptProjection = DictISO3166Excerpt.class)
public interface DictISO3166Repository extends BaseRepository<DictISO3166> {

    @RestResource(path = "page", rel = "page")
    Page<DictISO3166> findAllByNameContainingAndAlpha2CodeContainingAndCnNameContainingAndNumericCodeContaining(String name,
                                                                                                                String alpha2Code,
                                                                                                                String cnName,
                                                                                                                String numericCode,
                                                                                                                Pageable pageable);

}
