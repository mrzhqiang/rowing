package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

/**
 * 角色仓库。
 */
@RepositoryRestResource(path = "role", collectionResourceRel = "role")
public interface RoleRepository extends BaseRepository<Role> {

    @RestResource(path = "code", rel = "code")
    Optional<Role> findByCode(String code);

    @RestResource(exported = false)
    List<Role> findAllByCodeIn(List<String> codes);

}
