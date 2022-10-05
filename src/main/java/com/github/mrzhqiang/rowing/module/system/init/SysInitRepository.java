package com.github.mrzhqiang.rowing.module.system.init;

import com.github.mrzhqiang.rowing.module.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * 系统初始化仓库。
 */
@RepositoryRestResource(path = "/sys-init", collectionResourceRel = "/sys-init")
public interface SysInitRepository extends BaseRepository<SysInit> {

    /**
     * 判断指定路径是否存在。
     *
     * @param path 指定路径，通常是实现类的全限定名称。
     * @return 返回 true 表示指定路径已存在；否则表示不存在。
     */
    boolean existsByPath(String path);

    /**
     * 通过指定路径寻找系统初始化实体。
     *
     * @param path 指定路径。通常是实现类的全限定名称。
     * @return 返回可选的系统初始化实体。
     */
    Optional<SysInit> findByPath(String path);
}
