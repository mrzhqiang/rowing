package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.Access;
import java.util.Optional;

/**
 * 用户仓库。
 */
@RepositoryRestResource(path = "user", collectionResourceRel = "user")
public interface UserRepository extends BaseRepository<User> {

    @RestResource(exported = false)
    Optional<User> findByOwner(Account account);

    @RestResource(exported = false)
    Optional<User> findByOwner_Username(String username);
}
