package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * 初始化任务日志仓库。
 */
@RepositoryRestResource(path = "/init-task-log", collectionResourceRel = "/init-task-log")
public interface InitTaskLogRepository extends BaseRepository<InitTaskLog> {

}
