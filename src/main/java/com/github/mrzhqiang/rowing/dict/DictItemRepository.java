package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@PreAuthorize("hasRole('USER')")
@RepositoryRestResource(path = "dict-item", excerptProjection = DictItemExcerpt.class)
public interface DictItemRepository extends BaseRepository<DictItem> {

    @RestResource(path = "code-value", rel = "code-value")
    Optional<DictItem> findByGroup_CodeAndValue(String code, String value);

}
