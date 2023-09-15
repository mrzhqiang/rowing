package com.github.mrzhqiang.rowing.domain;

/**
 * 权限枚举。
 * <p>
 * 对于权限，以 AUTH_ 作为前缀，具有一定的格式，比如中间是 {@link BaseRepository 仓库} 的扩展类名称，以对应操作名称结尾，需保证唯一。
 * <p>
 * 权限用于接口授权：通常是执行方法之前，进行预授权，只有拥有相关权限标识符，才能执行成功，避免越权操作。
 * <p>
 * 权限相对角色来说，粒度更细，方式更灵活，需要搭配用户以及菜单来实现。
 * <p>
 * 注意：枚举值需改为大写字母，如果存在驼峰命名，则通过 _ 符号分离。
 *
 * @see com.github.mrzhqiang.rowing.util.Authorizes 授权工具。
 */
public enum Authority {

    AUTH_MENU_CREATE,
    AUTH_MENU_EDIT,
    AUTH_MENU_DELETE,

}
