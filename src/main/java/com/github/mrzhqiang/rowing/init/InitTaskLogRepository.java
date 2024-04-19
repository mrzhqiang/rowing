package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ADMIN')")
@RepositoryRestResource(path = "init-task-log", excerptProjection = InitTaskLogExcerpt.class)
public interface InitTaskLogRepository extends BaseRepository<InitTaskLog> {

}
