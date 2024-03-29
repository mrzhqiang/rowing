package com.github.mrzhqiang.rowing.dict.iso;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "dict-iso-639", excerptProjection = DictISO639Excerpt.class)
public interface DictISO639Repository extends BaseRepository<DictISO639> {

    @RestResource(path = "page", rel = "page")
    Page<DictISO639> findAllByNameContainingAndCodeContainingAndCnNameContaining(String name,
                                                                                 String code,
                                                                                 String cnName,
                                                                                 Pageable pageable);

}
