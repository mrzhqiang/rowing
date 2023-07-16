package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import static com.github.mrzhqiang.rowing.util.Authorizations.HAS_ROLE_ADMIN;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@PreAuthorize(HAS_ROLE_ADMIN)
@RepositoryRestResource(path = "setting", collectionResourceRel = "setting")
public interface SettingRepository extends BaseRepository<Setting> {

    Optional<Setting> findByName(String name);

}
