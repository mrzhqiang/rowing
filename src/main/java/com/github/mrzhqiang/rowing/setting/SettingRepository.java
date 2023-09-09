package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource(path = "setting", excerptProjection = SettingExcerpt.class)
public interface SettingRepository extends BaseRepository<Setting> {

    @RestResource(exported = false)
    Optional<Setting> findByCode(String code);

    @RestResource(path = "page", rel = "page")
    Page<Setting> findAllByNameContainingAndCodeContaining(String name, String code, Pageable pageable);

}
