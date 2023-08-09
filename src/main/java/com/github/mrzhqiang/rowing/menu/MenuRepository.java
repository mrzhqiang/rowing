package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.util.Authorizes;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 菜单仓库。
 * <p>
 */
@PreAuthorize(Authorizes.HAS_AUTHORITY_ADMIN)
@RepositoryRestResource(path = "menu", collectionResourceRel = "menu")
public interface MenuRepository extends BaseRepository<Menu> {

}
