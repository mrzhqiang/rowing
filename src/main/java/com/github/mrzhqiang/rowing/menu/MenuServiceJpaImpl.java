package com.github.mrzhqiang.rowing.menu;

import org.springframework.stereotype.Service;

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
