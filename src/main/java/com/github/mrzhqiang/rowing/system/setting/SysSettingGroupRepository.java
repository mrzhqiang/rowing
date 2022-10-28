package com.github.mrzhqiang.rowing.system.setting;

import com.github.mrzhqiang.rowing.domain.BaseRepository;

public interface SysSettingGroupRepository extends BaseRepository<SysSettingGroup> {

    boolean existsByCode(String code);
}
