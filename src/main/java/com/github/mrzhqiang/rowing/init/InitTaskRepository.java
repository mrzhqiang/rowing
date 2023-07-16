package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.util.Authorizations;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * 初始化任务仓库。
 *
 * @see RepositoryRestResource 表示当前仓库即 rest api 资源，无需写 controller 即可暴露 CRUD 接口。
 * @see RestResource 表示定制仓库方法，其中 exported = false 表示方法不会暴露为接口。
 */
@PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
@RepositoryRestResource(path = "init-task", collectionResourceRel = "init-task")
public interface InitTaskRepository extends BaseRepository<InitTask> {

    @RestResource(exported = false)
    boolean existsByPath(String path);

    @RestResource(exported = false)
    List<InitTask> findAllByDiscardAndPathNotIn(Logic discard, List<String> paths);

    InitTask findByPath(String path);
}
