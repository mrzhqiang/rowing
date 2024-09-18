import store from 'src/stores';

/**
 * 管理角色权限。
 *
 * @type {string}
 */
const MANAGER_ROLE = 'ROLE_ADMIN';

/**
 * 权限标记。
 */
export const PERMISSION_MARK = {
    menu: {
        create: [MANAGER_ROLE, 'AUTH_MENU_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_MENU_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_MENU_DELETE'],
    },
    menuResource: {
        create: [MANAGER_ROLE, 'AUTH_MENU_RESOURCE_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_MENU_RESOURCE_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_MENU_RESOURCE_DELETE'],
    },
    initTask: {
        edit: [MANAGER_ROLE, 'AUTH_INIT_TASK_EDIT'],
        execute: [MANAGER_ROLE, 'AUTH_INIT_TASK_EXECUTE'],
    },
    dict: {
        create: [MANAGER_ROLE, 'AUTH_DICT_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_DICT_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_DICT_DELETE'],
    },
    setting: {
        create: [MANAGER_ROLE, 'AUTH_SETTING_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_SETTING_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_SETTING_DELETE'],
    },
    account: {
        create: [MANAGER_ROLE, 'AUTH_ACCOUNT_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_ACCOUNT_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_ACCOUNT_DELETE'],
    },
    role: {
        create: [MANAGER_ROLE, 'AUTH_ROLE_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_ROLE_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_ROLE_DELETE'],
        account: [MANAGER_ROLE, 'AUTH_ROLE_ACCOUNT'],
        menu: [MANAGER_ROLE, 'AUTH_ROLE_MENU'],
        menuResource: [MANAGER_ROLE, 'AUTH_ROLE_MENU_RESOURCE'],
    },
    exam: {
        create: [MANAGER_ROLE, 'AUTH_EXAM_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_EXAM_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_EXAM_DELETE'],
        taker: [MANAGER_ROLE, 'AUTH_EXAM_TAKER'],
        takerStart: [MANAGER_ROLE, 'AUTH_EXAM_TAKER_START'],
        marker: [MANAGER_ROLE, 'AUTH_EXAM_MARKER'],
        markerStart: [MANAGER_ROLE, 'AUTH_EXAM_MARKER_START'],
    },
    examRule: {
        create: [MANAGER_ROLE, 'AUTH_EXAM_RULE_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_EXAM_RULE_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_EXAM_RULE_DELETE'],
    },
    examMode: {
        create: [MANAGER_ROLE, 'AUTH_EXAM_MODE_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_EXAM_MODE_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_EXAM_MODE_DELETE'],
    },
    examQuestionBank: {
        create: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_BANK_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_BANK_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_BANK_DELETE'],
    },
    examQuestion: {
        create: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_DELETE'],
    },
    examQuestionOption: {
        create: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_OPTION_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_OPTION_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_OPTION_DELETE'],
    },
    examPaper: {
        create: [MANAGER_ROLE, 'AUTH_EXAM_PAPER_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_EXAM_PAPER_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_EXAM_PAPER_DELETE'],
        taker: [MANAGER_ROLE, 'AUTH_EXAM_PAPER_TAKER'],
        marker: [MANAGER_ROLE, 'AUTH_EXAM_PAPER_MARKER'],
    },
    examPaperAnswerCard: {
        create: [MANAGER_ROLE, 'AUTH_EXAM_PAPER_ANSWER_CARD_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_EXAM_PAPER_ANSWER_CARD_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_EXAM_PAPER_ANSWER_CARD_DELETE'],
    },
    examPaperAnswer: {
        create: [MANAGER_ROLE, 'AUTH_EXAM_PAPER_ANSWER_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_EXAM_PAPER_ANSWER_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_EXAM_PAPER_ANSWER_DELETE'],
    },
    examQuestionReport: {
        create: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_REPORT_CREATE'],
        edit: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_REPORT_EDIT'],
        delete: [MANAGER_ROLE, 'AUTH_EXAM_QUESTION_REPORT_DELETE'],
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
        console.error(`need roles! Like v-permission="['ROLE_ADMIN','ROLE_USER']"`);
        return false;
    }
}
