package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.util.Authorizations;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 初始化任务日志仓库。
 */
@PreAuthorize(Authorizations.HAS_ROLE_ADMIN)
@RepositoryRestResource(path = "init-task-log", collectionResourceRel = "init-task-log")
public interface InitTaskLogRepository extends BaseRepository<InitTaskLog> {

}
