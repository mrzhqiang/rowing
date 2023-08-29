package com.github.mrzhqiang.rowing.action;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "action-log", collectionResourceRel = "action-log")
public interface ActionLogRepository extends BaseRepository<ActionLog> {
}
