package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.domain.Logic;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * 初始化任务仓库。
 */
@RepositoryRestResource(path = "/init-task", collectionResourceRel = "/init-task")
public interface InitTaskRepository extends BaseRepository<InitTask> {

    /**
     * 通过废弃字段找到所有相关的初始化任务。
     *
     * @return 初始化任务列表。
     */
    List<InitTask> findAllByDiscard(Logic discard);

    /**
     * 判断指定路径是否存在。
     *
     * @param path 指定路径，通常是实现类的全限定名称。
     * @return 返回 true 表示指定路径已存在；否则表示不存在。
     */
    boolean existsByPath(String path);

    /**
     * 找到所有不属于指定路径列表的初始化任务数据。
     *
     * @param paths 指定路径列表。
     * @return 初始化任务数据。
     */
    List<InitTask> findAllByPathNotIn(List<String> paths);

    /**
     * 通过指定路径寻找系统初始化实体。
     *
     * @param path 指定路径。通常是实现类的全限定名称。
     * @return 返回可选的系统初始化实体。
     */
    Optional<InitTask> findByPath(String path);
}
