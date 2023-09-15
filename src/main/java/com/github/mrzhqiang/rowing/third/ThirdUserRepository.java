package com.github.mrzhqiang.rowing.third;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "third-user", collectionResourceRel = "third-user")
public interface ThirdUserRepository extends BaseRepository<ThirdUser> {

    Optional<ThirdUser> findByAccount_Username(String username);
}
