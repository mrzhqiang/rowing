package com.github.mrzhqiang.rowing.action;

import com.github.mrzhqiang.rowing.domain.ActionType;
import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('USER')")
@RepositoryRestResource(path = "action-log", excerptProjection = ActionLogExcerpt.class)
public interface ActionLogRepository extends BaseRepository<ActionLog> {

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull
    @Override
    Optional<ActionLog> findById(@NotNull Long id);

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull
    @Override
    Page<ActionLog> findAll(@NotNull Pageable pageable);

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull
    @Override
    List<ActionLog> findAll(@NotNull Sort sort);

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull
    @Override
    List<ActionLog> findAll();

    @PreAuthorize("hasRole('ADMIN')")
    @RestResource(path = "page", rel = "page")
    @Query("select a from ActionLog a where (:type is null or a.type = :type)")
    Page<ActionLog> pageByActionType(ActionType type, Pageable pageable);

}
