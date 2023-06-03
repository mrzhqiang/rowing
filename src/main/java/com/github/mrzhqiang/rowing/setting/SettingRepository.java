package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.repository.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "setting", collectionResourceRel = "setting")
public interface SettingRepository extends BaseRepository<Setting> {

}
