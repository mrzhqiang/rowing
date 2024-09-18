package com.github.mrzhqiang.rowing.dict.gbt;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasRole('USER')")
@RepositoryRestResource(path = "dict-gbt-2260", excerptProjection = DictGBT2260Excerpt.class)
public interface DictGBT2260Repository extends BaseRepository<DictGBT2260> {

    @RestResource(path = "tree", rel = "tree")
    List<DictGBT2260> findAllByParentIsNullOrderByCodeAscCreatedDesc();

    @RestResource(path = "page", rel = "page")
    Page<DictGBT2260> findAllByNameContainingAndCodeContainingAndLevelContaining(String name,
                                                                                 String code,
                                                                                 String level,
                                                                                 Pageable pageable);

}
