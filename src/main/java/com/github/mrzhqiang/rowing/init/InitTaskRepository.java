package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.util.Authorizations;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * 初始化任务仓库。
 */
@PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
@RepositoryRestResource(path = "init-task", collectionResourceRel = "init-task")
public interface InitTaskRepository extends BaseRepository<InitTask> {

    /**
     * 判断指定路径是否存在。
     *
     * @param path 指定路径，通常是实现类的全限定名称。
     * @return 返回 true 表示指定路径已存在；否则表示不存在。
     */
    boolean existsByPath(String path);

    /**
     * 通过废弃字段找到所有不在指定路径列表中的初始化任务。
     *
     * @param discard 废弃字段。
     * @param paths   指定路径列表。
     * @return 初始化任务列表。
     */
    List<InitTask> findAllByDiscardAndPathNotIn(Logic discard, List<String> paths);

    /**
     * 通过指定路径找到初始化任务。
     *
     * @param path 指定路径。通常是实现类的全限定名称。
     * @return 初始化任务。
     */
    InitTask findByPath(String path);

}
