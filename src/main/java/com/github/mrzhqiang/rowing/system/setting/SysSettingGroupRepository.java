package com.github.mrzhqiang.rowing.system.setting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SysSettingGroupRepository extends JpaRepository<SysSettingGroup, Long> {

    boolean existsByCode(String code);
}
