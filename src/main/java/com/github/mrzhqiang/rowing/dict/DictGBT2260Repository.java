package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.util.Authorizes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * GB/T 2260 标准仓库。
 * <p>
 */
@PreAuthorize(Authorizes.HAS_AUTHORITY_ADMIN)
@RepositoryRestResource(path = "dict-gbt-2260", collectionResourceRel = "dicts",
        excerptProjection = DictGBT2260Excerpt.class)
public interface DictGBT2260Repository extends BaseRepository<DictGBT2260> {

    @RestResource(path = "tree", rel = "tree")
    List<DictGBT2260> findAllByParentIsNullOrderByCodeAscCreatedDesc();

    @RestResource(path = "page", rel = "page")
    Page<DictGBT2260> findAllByNameContainingAndCodeContainingAndLevelContaining(String name,
                                                                                 String code,
                                                                                 String level,
                                                                                 Pageable pageable);

}
