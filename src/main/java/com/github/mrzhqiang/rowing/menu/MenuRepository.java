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
@RepositoryRestResource(path = "menu", excerptProjection = MenuExcerpt.class)
public interface MenuRepository extends BaseRepository<Menu> {

    @RestResource(exported = false)
    List<Menu> findAllByParentIsNullAndEnabled(Logic enabled, Sort sort);

    @RestResource(path = "tree", rel = "tree")
    List<Menu> findAllByParentIsNullOrderByOrderedAscCreatedDesc();

    /**
     * 根据标题和路径查询所有菜单分页数据。
     * <p>
     * 注意：必须是 Containing 关键字且前端明确传递 title 以及 path 参数，才能正常查询。
     * <p>
     * 如果漏掉参数名称或者使用 Like 关键字的话，将无法保证预期的查询结果。
     *
     * @param title    标题参数。
     * @param path     路径参数。
     * @param pageable 分页参数。
     * @return 菜单分页数据。
     */
    @RestResource(path = "page", rel = "page")
    Page<Menu> findAllByTitleContainingAndPathContaining(String title, String path, Pageable pageable);

}
