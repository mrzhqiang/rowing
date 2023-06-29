package com.github.mrzhqiang.rowing.exception;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/exception", collectionResourceRel = "/exception")
public interface ExceptionLogRepository extends BaseRepository<ExceptionLog> {

}
