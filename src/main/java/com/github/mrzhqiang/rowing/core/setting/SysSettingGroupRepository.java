package com.github.mrzhqiang.rowing.core.setting;

import com.github.mrzhqiang.rowing.core.domain.BaseRepository;

public interface SysSettingGroupRepository extends BaseRepository<SysSettingGroup> {

    boolean existsByCode(String code);
}
