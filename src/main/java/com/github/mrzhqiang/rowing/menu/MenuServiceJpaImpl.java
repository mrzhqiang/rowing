package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.config.MenuProperties;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.role.RoleRepository;
import com.github.mrzhqiang.rowing.util.Jsons;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuServiceJpaImpl implements MenuService {

    private final MenuProperties properties;
    private final MenuMapper mapper;
    private final MenuRepository repository;
    private final RoleRepository roleRepository;

    public MenuServiceJpaImpl(MenuProperties properties,
                              MenuMapper mapper,
                              MenuRepository repository,
                              RoleRepository roleRepository) {
        this.properties = properties;
        this.mapper = mapper;
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void init() {
        List<String> jsonPaths = properties.getJsonPaths();
        if (!CollectionUtils.isEmpty(jsonPaths)) {
            jsonPaths.forEach(this::createMenu);
        }
    }

    @SneakyThrows
    private void createMenu(String jsonPath) {
        Preconditions.checkNotNull(jsonPath, "menu json file == null");

        File jsonFile = ResourceUtils.getFile(jsonPath);
        Preconditions.checkArgument(jsonFile.exists(), "menu json file must be exists");
        Preconditions.checkArgument(!jsonFile.isDirectory(), "menu json file must be not directory");

        List<MenuRoute> menuRoutes = Jsons.listFrom(jsonFile, MenuRoute.class);
        if (CollectionUtils.isEmpty(menuRoutes)) {
            log.warn("cannot parse menu routes from {}", jsonPath);
            return;
        }

        for (int i = 0; i < menuRoutes.size(); i++) {
            createRootMenu(i, menuRoutes.get(i));
        }
    }

    private void createRootMenu(int ordered, MenuRoute route) {
        Menu rootMenu = mapper.toEntity(route);
        // 顶级菜单，完整路径就是它自己的路径
        rootMenu.setFullPath(route.getPath());
        rootMenu.setOrdered(ordered);
        repository.save(rootMenu);
        log.info("create the root menu {} is successful", rootMenu);

        bindingRole(route, rootMenu);

        List<MenuRoute> children = route.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            log.info("this menu route {} without children", route);
            return;
        }

        for (int i = 0; i < children.size(); i++) {
            createChildrenMenu(i, rootMenu, children.get(i));
        }
    }

    private void bindingRole(MenuRoute route, Menu menu) {
        if (route == null || menu == null) {
            return;
        }

        MenuRoute.Meta meta = route.getMeta();
        if (meta != null) {
            List<String> roles = meta.getRoles();
            if (CollectionUtils.isEmpty(roles)) {
                return;
            }

            // 不想过度设计复杂的数据结构，去实现一个角色绑定多个菜单，逐个绑定感觉更清晰
            roleRepository.findAllByCodeIn(roles).forEach(it -> {
                List<Menu> menuList = it.getMenuList();
                if (menuList.contains(menu)) {
                    return;
                }
                menuList.add(menu);
                roleRepository.save(it);
            });
        }
    }

    private void createChildrenMenu(int ordered, Menu parent, MenuRoute data) {
        Menu menu = mapper.toEntity(data);
        // 子级菜单，使用上级菜单路径拼接当前菜单路径，作为完整路径
        menu.setFullPath(concatFullPath(parent, data));
        menu.setOrdered(ordered);
        menu.setParent(parent);
        repository.save(menu);
        log.info("create the menu {} is successful", menu);

        bindingRole(data, menu);

        List<MenuRoute> children = data.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            log.warn("this menu {} cannot find children", data);
            return;
        }

        for (int i = 0; i < children.size(); i++) {
            createChildrenMenu(i, menu, children.get(i));
        }
    }

    private String concatFullPath(Menu parent, MenuRoute data) {
        String fullPath = parent.getFullPath();
        if (PATH_SEPARATOR.equals(fullPath)) {
            return fullPath.concat(data.getPath());
        }
        return fullPath.concat(PATH_SEPARATOR).concat(data.getPath());
    }

    @Override
    public List<MenuRoute> findRoutes() {
        Sort sort = Sort.sort(Menu.class).by(Menu::getOrdered).ascending()
                .and(Sort.sort(Menu.class).by(Menu::getCreated).descending());
        return repository.findAllByParentIsNullAndEnabled(Logic.YES, sort).stream()
                .map(mapper::toRoute)
                .collect(Collectors.toList());
    }

}
