package com.github.mrzhqiang.rowing.menu;

import org.springframework.stereotype.Service;

/**
 * 菜单服务 JPA 实现。
 * <p>
 */
@Service
public class MenuServiceJpaImpl implements MenuService {

    private final MenuMapper mapper;
    private final MenuRepository repository;

    public MenuServiceJpaImpl(MenuMapper mapper,
                              MenuRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }
}
