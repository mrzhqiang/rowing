package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Enums;

/**
 * 权限枚举。
 * <p>
 * 对于权限，以 AUTH_ 作为前缀，具有一定的格式，比如中间是 {@link BaseRepository 仓库} 的扩展类名称，以对应操作名称结尾，需保证唯一。
 * <p>
 * 权限用于接口授权：通常是执行方法之前，进行预授权，只有拥有相关权限标识符，才能执行成功，避免越权操作。
 * <p>
 * 权限相对角色来说，粒度更细，方式更灵活。
 * <p>
 * 通常在后台添加权限到角色中，此时权限列表的数据来源就是这里的枚举值，作为内置数据字典存放在字典项中。
 * <p>
 * 注意：枚举值需改为大写字母，如果存在驼峰命名，则通过 _ 符号分割。枚举值与前端权限声明和后端接口注解需保持一致。
 */
public enum Authority {

    /**
     * 创建菜单权限。
     */
    AUTH_MENU_CREATE,
    /**
     * 编辑菜单权限。
     */
    AUTH_MENU_EDIT,
    /**
     * 删除菜单权限。
     */
    AUTH_MENU_DELETE,
    ;

    public static Authority of(String auth) {
        return Enums.findByNameIgnoreCase(Authority.class, auth, null);
    }

}
