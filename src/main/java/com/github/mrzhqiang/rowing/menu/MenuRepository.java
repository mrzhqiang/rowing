package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.util.Authorizes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * 菜单仓库。
 * <p>
 */
@PreAuthorize(Authorizes.HAS_AUTHORITY_ADMIN)
@RepositoryRestResource(path = "menu", collectionResourceRel = "menu", excerptProjection = MenuExcerpt.class)
public interface MenuRepository extends BaseRepository<Menu> {

    @RestResource(exported = false)
    List<Menu> findAllByParentIsNullAndEnabled(Logic enabled, Sort sort);

    @SuppressWarnings("unused")
    @RestResource(path = "root", rel = "root")
    Page<Menu> findAllByParentIsNull(Pageable pageable);

}
