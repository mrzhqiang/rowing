package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.role.Role;
import com.github.mrzhqiang.rowing.util.Validations;
import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import com.google.common.base.Preconditions;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 菜单工具。
 * <p>
 * 提供菜单相关的常量及方法。
 */
public final class Menus {
    private Menus() {
        // no instances.
    }

    /**
     * 菜单路径分隔符。
     */
    public static final String PATH_SEPARATOR = "/";

    /**
     * 验证菜单创建。
     *
     * @param menu 菜单实体。
     */
    public static void validateCreate(Menu menu) {
        Preconditions.checkNotNull(menu, "menu == null");

        Menu parent = menu.getParent();
        if (parent != null) {
            Preconditions.checkArgument(Validations.validateUrl(parent.getPath()),
                    "操作失败！上级菜单不能是一个外部链接，无法创建");
        }
    }

    public static void validateDelete(Menu menu) {
        Preconditions.checkNotNull(menu, "menu == null");

        List<Menu> children = menu.getChildren();
        Preconditions.checkArgument(CollectionUtils.isEmpty(children),
                "操作失败！当前菜单存在下级菜单，无法删除");
    }

    /**
     * 处理名称。
     *
     * @param menu 菜单实体。
     */
    public static void handleName(Menu menu) {
        if (!StringUtils.hasText(menu.getName())) {
            menu.setName(LOWER_HYPHEN.to(UPPER_CAMEL, menu.getPath()));
        }
    }

    /**
     * 处理完整路径。
     *
     * @param menu 菜单实体。
     */
    public static void handleFullPath(Menu menu) {
        if (!StringUtils.hasText(menu.getFullPath())) {
            String fullPath = Optional.ofNullable(menu.getParent())
                    .map(Menu::getFullPath)
                    // 子级菜单，使用上级菜单路径拼接当前菜单路径，作为完整路径
                    .map(it -> Menus.concatFullPath(it, menu.getPath()))
                    // 顶级菜单，完整路径就是路径
                    .orElse(menu.getPath());
            menu.setFullPath(fullPath);
        }
    }

    /**
     * 拼接完整路径。
     * <p>
     * 完整路径 = 上级菜单的完整路径 + 当前菜单的路径。
     * <p>
     * 如果当前菜单的路径已经是一个链接，那么不做任何拼接，直接返回。
     *
     * @param parentFullPath 上级菜单的完整路径。
     * @param path           当前菜单的路径。
     * @return 完整路径。
     */
    public static String concatFullPath(String parentFullPath, String path) {
        if (Validations.validateUrl(path)) {
            return path;
        }
        if (PATH_SEPARATOR.equals(parentFullPath) || parentFullPath.endsWith(PATH_SEPARATOR)) {
            return parentFullPath.concat(path);
        }
        return parentFullPath.concat(PATH_SEPARATOR).concat(path);
    }

    /**
     * 转换角色标识符列表。
     *
     * @param roleList 角色列表。
     * @return 角色标识符列表。
     */
    public static List<String> convertRoles(List<Role> roleList) {
        return roleList.stream().map(Role::getCode).collect(Collectors.toList());
    }
}
