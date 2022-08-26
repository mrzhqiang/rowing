package com.github.mrzhqiang.rowing.core.init;

import com.github.mrzhqiang.rowing.core.domain.BaseRepository;

import java.util.Optional;

/**
 * 系统初始化仓库。
 * <p>
 * 用于提供 CURD 数据库查询功能，同时借助 rest 框架自动实现 CURD HTTP API 接口。
 */
public interface SysInitRepository extends BaseRepository<SysInit> {

    boolean existsByName(String name);

    Optional<SysInit> findByName(String name);
}
