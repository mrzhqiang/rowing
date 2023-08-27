package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.util.Authorizes;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.persistence.OrderBy;
import java.util.List;

/**
 * 菜单仓库。
 * <p>
 */
@PreAuthorize(Authorizes.HAS_AUTHORITY_ADMIN)
@RepositoryRestResource(path = "menu", collectionResourceRel = "menu")
public interface MenuRepository extends BaseRepository<Menu> {

    @OrderBy
    @RestResource(exported = false)
    List<Menu> findAllByEnabledIsTrueAndParentIsNull(Sort sort);

}
