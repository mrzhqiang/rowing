package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.domain.Logic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RepositoryRestResource(path = "role", excerptProjection = RoleExcerpt.class)
public interface RoleRepository extends BaseRepository<Role> {

    @RestResource(exported = false)
    Optional<Role> findByCodeAndImmutableIsTrue(String code);

    @RestResource(exported = false)
    List<Role> findAllByCodeIn(List<String> codes);

    @RestResource(path = "code", rel = "code")
    Optional<Role> findByCode(String code);

    @RestResource(path = "page", rel = "page")
    Page<Role> findAllByNameContainingAndCodeContaining(String name, String code, Pageable pageable);

}
