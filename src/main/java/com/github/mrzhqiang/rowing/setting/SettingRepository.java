package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@RepositoryRestResource(path = "setting", collectionResourceRel = "setting")
public interface SettingRepository extends BaseRepository<Setting> {

    @PermitAll
    Optional<Setting> findByName(String name);

}
