package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@PreAuthorize("hasRole('USER')")
@RepositoryRestResource(path = "dict", collectionResourceRel = "dicts", excerptProjection = DictGroupExcerpt.class)
public interface DictGroupRepository extends BaseRepository<DictGroup> {

    @RestResource(path = "code", rel = "code")
    Optional<DictGroup> findByCode(String code);

    @RestResource(path = "page", rel = "page")
    Page<DictGroup> findAllByNameContainingAndCodeContaining(String name, String code, Pageable pageable);

}
