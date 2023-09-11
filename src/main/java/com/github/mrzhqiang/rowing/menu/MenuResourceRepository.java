package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "menu-resource", excerptProjection = MenuResourceExcerpt.class)
public interface MenuResourceRepository extends BaseRepository<MenuResource> {

}
