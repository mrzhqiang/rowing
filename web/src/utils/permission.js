import store from '@/store';

/**
 * 管理员角色权限。
 *
 * @type {string}
 */
const ADMIN_ROLE = 'ADMIN';

/**
 * 权限标记。
 */
export const PERMISSION_MARK = {
    menu: {
        create: [ADMIN_ROLE, 'AUTH_MENU_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_MENU_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_MENU_DELETE'],
    },
    initTask: {
        edit: [ADMIN_ROLE, 'AUTH_INIT_TASK_EDIT'],
        execute: [ADMIN_ROLE, 'AUTH_INIT_TASK_EXECUTE'],
    },
    dict: {
        create: [ADMIN_ROLE, 'AUTH_DICT_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_DICT_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_DICT_DELETE'],
    },
    setting: {
        create: [ADMIN_ROLE, 'AUTH_SETTING_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_SETTING_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_SETTING_DELETE'],
    },
};

/**
 * 检测是否拥有指定权限。
 * @param {Array} value
 * @returns {Boolean}
 * @example see @/views/permission/directive.vue
 */
export default function checkPermission(value) {
    if (value && value instanceof Array && value.length > 0) {
        const roles = store.getters && store.getters.roles;
        const permissionRoles = value;

        return roles.some(role => {
            return permissionRoles.includes(role);
        });
    } else {
        console.error(`need roles! Like v-permission="['ADMIN','USER']"`);
        return false;
    }
}
