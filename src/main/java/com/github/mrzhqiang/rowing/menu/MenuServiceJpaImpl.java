package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.role.RoleService;
import com.github.mrzhqiang.rowing.util.Jsons;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

/**
 * 菜单服务的 JPA 实现。
 */
@Slf4j
@EnableConfigurationProperties(MenuProperties.class)
@Service
public class MenuServiceJpaImpl implements MenuService {

    private final MenuProperties properties;
    private final MenuMapper mapper;
    private final MenuRepository repository;
    private final RoleService roleService;

    public MenuServiceJpaImpl(MenuProperties properties,
                              MenuMapper mapper,
                              MenuRepository repository,
                              RoleService roleService) {
        this.properties = properties;
        this.mapper = mapper;
        this.repository = repository;
        this.roleService = roleService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void init() {
        List<String> jsonPaths = properties.getJsonPaths();
        if (!CollectionUtils.isEmpty(jsonPaths)) {
            jsonPaths.forEach(this::initFromJson);
        }
    }

    @SneakyThrows
    private void initFromJson(String jsonPath) {
        Preconditions.checkNotNull(jsonPath, "menu json file == null");
        File jsonFile = ResourceUtils.getFile(jsonPath);
        Preconditions.checkArgument(jsonFile.exists(), "menu json file must be exists");
        Preconditions.checkArgument(!jsonFile.isDirectory(), "menu json file must be not directory");

        List<MenuData> menuDataList = Jsons.listFrom(jsonFile, MenuData.class);
        if (CollectionUtils.isEmpty(menuDataList)) {
            log.warn("cannot find menu data list from {}", jsonPath);
            return;
        }

        for (int i = 0; i < menuDataList.size(); i++) {
            createRootMenu(i, menuDataList.get(i));
        }
    }

    private void createRootMenu(int ordered, MenuData data) {
        // 顶级菜单，完整路径就是它自己的路径
        data.setFullPath(data.getPath());
        data.setOrdered(ordered);

        Menu menu = mapper.toEntity(data);
        repository.save(menu);
        log.info("create the root menu {} is successful", menu);

        bindingRole(data, menu);

        List<MenuData> children = data.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            log.warn("this root menu {} cannot find children", data);
            return;
        }

        for (int i = 0; i < children.size(); i++) {
            createChildrenMenu(i, menu, children.get(i));
        }
    }

    private void bindingRole(MenuData data, Menu menu) {
        if (menu == null || data == null) {
            return;
        }

        MenuMetaData meta = data.getMeta();
        if (meta != null) {
            List<String> roles = meta.getRoles();
            if (CollectionUtils.isEmpty(roles)) {
                return;
            }

            roles.forEach(it -> roleService.bindingMenu(it, menu));
        }
    }

    private void createChildrenMenu(int ordered, Menu parent, MenuData data) {
        // 子级菜单，使用上级菜单路径拼接当前菜单路径，作为完整路径
        data.setFullPath(concatFullPath(parent, data));
        data.setOrdered(ordered);

        Menu menu = mapper.toEntity(data);
        menu.setParent(parent);
        repository.save(menu);
        log.info("create the menu {} is successful", menu);

        bindingRole(data, menu);

        List<MenuData> children = data.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            log.warn("this menu {} cannot find children", data);
            return;
        }

        for (int i = 0; i < children.size(); i++) {
            createChildrenMenu(i, menu, children.get(i));
        }
    }

    private String concatFullPath(Menu parent, MenuData data) {
        String fullPath = parent.getFullPath();
        if (PATH_SEPARATOR.equals(fullPath)) {
            return fullPath.concat(data.getPath());
        }
        return fullPath.concat(PATH_SEPARATOR).concat(data.getPath());
    }

}
