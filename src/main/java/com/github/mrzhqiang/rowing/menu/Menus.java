package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.role.Role;
import com.github.mrzhqiang.rowing.util.Validations;
import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单工具。
 * <p>
 * 提供菜单相关的常量及方法。
 */
@UtilityClass
public class Menus {

    /**
     * 菜单路径分隔符。
     */
    public static final String PATH_SEPARATOR = "/";

    /**
     * 验证菜单创建。
     *
     * @param menu 新的菜单实体。
     */
    public static void validateCreate(Menu menu) {
        Preconditions.checkNotNull(menu, "menu == null");

        Menu parent = menu.getParent();
        if (parent != null) {
            Preconditions.checkArgument(!Validations.validUrl(parent.getPath()),
                    "操作失败！上级菜单不能是一个外部链接，无法创建");
        }
    }

    /**
     * 验证菜单更新。
     * <p>
     * 注意：对于 PATCH 接口，菜单实体是合并了更新内容的数据，如果用户恶意传递内置字段为 false 的话，这个校验会得到通过，
     * 因此我们设定内置字段无法更新，保证即使这里的校验得到通过，也会因为触发无法修改内置字段策略，而使得恶意攻击无效。
     *
     * @param menu 菜单实体。
     */
    public static void validateSave(Menu menu) {
        Preconditions.checkNotNull(menu, "menu == null");
        // 建议通过修改 classpath:data/menu-routes.json 文件更新内置菜单，然后重新执行一次菜单初始化即可
        Preconditions.checkArgument(Logic.NO.equals(menu.getInternal()), "操作失败！内置菜单，无法更新");
    }

    /**
     * 验证菜单删除。
     *
     * @param menu 菜单实体。
     */
    public static void validateDelete(Menu menu) {
        Preconditions.checkNotNull(menu, "menu == null");
        Preconditions.checkArgument(Logic.NO.equals(menu.getInternal()), "操作失败！内置菜单，无法删除");
        Preconditions.checkArgument(CollectionUtils.isEmpty(menu.getChildren()),
                "操作失败！当前菜单存在下级菜单，无法删除");
    }

    /**
     * 处理名称。
     *
     * @param menu 菜单实体。
     */
    public static void handleName(Menu menu) {
        String path = menu.getPath();
        if (path.contains("/")) {
            path = path.substring(0, path.lastIndexOf("/"));
        }
        menu.setName(LOWER_HYPHEN.to(UPPER_CAMEL, path));
    }

    /**
     * 处理完整路径。
     *
     * @param menu 菜单实体。
     */
    public static void handleFullPath(Menu menu) {
        String fullPath = Optional.ofNullable(menu.getParent())
                .map(Menu::getFullPath)
                // 子级菜单，使用上级菜单路径拼接当前菜单路径，作为完整路径
                .map(it -> Menus.concatFullPath(it, menu.getPath()))
                // 顶级菜单，完整路径就是路径
                .orElse(menu.getPath());
        menu.setFullPath(fullPath);
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
        if (Validations.validUrl(path)) {
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
    public static List<String> convertRoles(Set<Role> roleList) {
        return roleList.stream().map(Role::getCode).collect(Collectors.toList());
    }

}
