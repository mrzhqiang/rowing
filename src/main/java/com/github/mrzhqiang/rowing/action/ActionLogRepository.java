package com.github.mrzhqiang.rowing.action;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "action-log", excerptProjection = ActionLogExcerpt.class)
public interface ActionLogRepository extends BaseRepository<ActionLog> {

    @RestResource(path = "page", rel = "page")
    Page<ActionLog> findAllByActionContaining(String action, Pageable pageable);

}
