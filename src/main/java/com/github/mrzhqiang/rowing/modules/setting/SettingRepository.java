package com.github.mrzhqiang.rowing.modules.setting;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "setting", collectionResourceRel = "setting")
public interface SettingRepository extends BaseRepository<Setting> {

}
