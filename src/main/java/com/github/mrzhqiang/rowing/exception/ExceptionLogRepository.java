package com.github.mrzhqiang.rowing.exception;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "exception-log", excerptProjection = ExceptionLogExcerpt.class)
public interface ExceptionLogRepository extends BaseRepository<ExceptionLog> {

    @RestResource(path = "page", rel = "page")
    Page<ExceptionLog> findAllByUrlContainingAndCodeContaining(String url, String code, Pageable pageable);

}
