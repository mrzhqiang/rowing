package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.domain.Logic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RepositoryRestResource(path = "init-task", excerptProjection = InitTaskExcerpt.class)
public interface InitTaskRepository extends BaseRepository<InitTask> {

    @RestResource(exported = false)
    boolean existsByPath(String path);

    @RestResource(exported = false)
    List<InitTask> findAllByDiscardAndPathNotIn(Logic discard, List<String> paths);

    @RestResource(exported = false)
    InitTask findByPath(String path);

    @RestResource(path = "page", rel = "page")
    Page<InitTask> findAllByNameContaining(String name, Pageable pageable);

}
