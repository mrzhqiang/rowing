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
    menuResource: {
        create: [ADMIN_ROLE, 'AUTH_MENU_RESOURCE_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_MENU_RESOURCE_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_MENU_RESOURCE_DELETE'],
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
    account: {
        create: [ADMIN_ROLE, 'AUTH_ACCOUNT_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_ACCOUNT_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_ACCOUNT_DELETE'],
    },
    role: {
        create: [ADMIN_ROLE, 'AUTH_ROLE_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_ROLE_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_ROLE_DELETE'],
        account: [ADMIN_ROLE, 'AUTH_ROLE_ACCOUNT'],
        menu: [ADMIN_ROLE, 'AUTH_ROLE_MENU'],
        menuResource: [ADMIN_ROLE, 'AUTH_ROLE_MENU_RESOURCE'],
    },
    exam: {
        create: [ADMIN_ROLE, 'AUTH_EXAM_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_EXAM_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_EXAM_DELETE'],
        taker: [ADMIN_ROLE, 'AUTH_EXAM_TAKER'],
        takerStart: [ADMIN_ROLE, 'AUTH_EXAM_TAKER_START'],
        marker: [ADMIN_ROLE, 'AUTH_EXAM_MARKER'],
        markerStart: [ADMIN_ROLE, 'AUTH_EXAM_MARKER_START'],
    },
    examRule: {
        create: [ADMIN_ROLE, 'AUTH_EXAM_RULE_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_EXAM_RULE_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_EXAM_RULE_DELETE'],
    },
    examMode: {
        create: [ADMIN_ROLE, 'AUTH_EXAM_MODE_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_EXAM_MODE_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_EXAM_MODE_DELETE'],
    },
    examQuestionBank: {
        create: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_BANK_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_BANK_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_BANK_DELETE'],
    },
    examQuestion: {
        create: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_DELETE'],
    },
    examQuestionOption: {
        create: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_OPTION_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_OPTION_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_OPTION_DELETE'],
    },
    examPaper: {
        create: [ADMIN_ROLE, 'AUTH_EXAM_PAPER_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_EXAM_PAPER_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_EXAM_PAPER_DELETE'],
        taker: [ADMIN_ROLE, 'AUTH_EXAM_PAPER_TAKER'],
        marker: [ADMIN_ROLE, 'AUTH_EXAM_PAPER_MARKER'],
    },
    examPaperAnswerCard: {
        create: [ADMIN_ROLE, 'AUTH_EXAM_PAPER_ANSWER_CARD_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_EXAM_PAPER_ANSWER_CARD_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_EXAM_PAPER_ANSWER_CARD_DELETE'],
    },
    examPaperAnswer: {
        create: [ADMIN_ROLE, 'AUTH_EXAM_PAPER_ANSWER_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_EXAM_PAPER_ANSWER_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_EXAM_PAPER_ANSWER_DELETE'],
    },
    examQuestionReport: {
        create: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_REPORT_CREATE'],
        edit: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_REPORT_EDIT'],
        delete: [ADMIN_ROLE, 'AUTH_EXAM_QUESTION_REPORT_DELETE'],
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
