package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "setting", collectionResourceRel = "setting")
public interface SettingRepository extends BaseRepository<Setting> {

    /**
     * 通过名称找到设置。
     *
     * @param name 名称。
     * @return 可选的设置。
     */
    Optional<Setting> findByName(String name);
}
