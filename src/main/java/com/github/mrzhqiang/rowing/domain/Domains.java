package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.helper.Exceptions;

/**
 * 领域工具。
 * <p>
 * 主要定义一些常量以及方法。
 */
public final class Domains {
    private Domains() {
        // no instances.
    }

    /**
     * 枚举名称长度。
     */
    public static final int ENUM_NAME_LENGTH = 50;
    /**
     * 用户名长度。
     */
    public static final int USERNAME_LENGTH = 24;
    /**
     * 类名称长度。
     */
    public static final int CLASS_NAME_LENGTH = 500;
    /**
     * 方法名称长度。
     */
    public static final int METHOD_NAME_LENGTH = 200;
    /**
     * 方法参数长度。
     */
    public static final int METHOD_PARAMS_LENGTH = 2000;
    /**
     * IP 地址长度。
     */
    public static final int IP_ADDRESS_LENGTH = 128;
    /**
     * 地理位置长度。
     */
    public static final int LOCATION_LENGTH = 100;
    /**
     * 设备信息长度。
     */
    public static final int DEVICE_INFO_LENGTH = 200;

    /**
     * 字典名称长度。
     */
    public static final int DICT_NAME_LENGTH = 100;
    /**
     * 字典代码长度。
     */
    public static final int DICT_CODE_LENGTH = 50;
    /**
     * 字典标签长度。
     */
    public static final int DICT_LABEL_LENGTH = 100;
    /**
     * 字典值长度。
     */
    public static final int DICT_VALUE_LENGTH = 50;
    /**
     * HTTP 状态原因短语长度。
     */
    public static final int HTTP_STATUS_REASON_PHRASE_LENGTH = 100;
    /**
     * HTTP 方法长度。
     */
    public static final int HTTP_METHOD_LENGTH = 10;
    /**
     * HTTP URL 请求路径长度。
     */
    public static final int HTTP_URL_PATH_LENGTH = 1024;
    /**
     * HTTP URL 查询参数长度。
     */
    public static final int HTTP_URL_QUERY_LENGTH = 6 * 1024;
    /**
     * 会话 ID 长度。
     */
    public static final int SESSION_ID_LENGTH = 36;
    /**
     * 异常消息长度。
     */
    public static final int EXCEPTION_MESSAGE_LENGTH = 500;
    /**
     * 异常代码长度。
     */
    public static final int EXCEPTION_CODE_LENGTH = 20;
    /**
     * 异常堆栈长度。
     */
    public static final int EXCEPTION_TRACE_LENGTH = Exceptions.MAX_TRACE_LENGTH;
    /**
     * 初始化任务名称长度。
     */
    public static final int INIT_TASK_NAME_LENGTH = 200;
    /**
     * 菜单路径长度。
     */
    public static final int MENU_PATH_LENGTH = 50;
    /**
     * 菜单组件长度。
     */
    public static final int MENU_COMPONENT_LENGTH = 200;
    /**
     * 菜单标题长度。
     */
    public static final int MENU_TITLE_LENGTH = 20;
    /**
     * 菜单图标长度。
     */
    public static final int MENU_ICON_LENGTH = 50;
    /**
     * 角色名称最大长度。
     */
    public static final int ROLE_NAME_LENGTH = 24;
    /**
     * 角色代码最大长度。
     */
    public static final int ROLE_CODE_LENGTH = 48;
    /**
     * 设置名称长度。
     */
    public static final int SETTING_NAME_LENGTH = 100;
    /**
     * 设置代码长度。
     */
    public static final int SETTING_CODE_LENGTH = 50;
    /**
     * 设置内容长度。
     */
    public static final int SETTING_CONTENT_LENGTH = 5000;
    /**
     * 设置风格长度。
     */
    public static final int SETTING_STYLE_LENGTH = 200;
    /**
     * 用户昵称长度。
     */
    public static final int USER_NICKNAME_LENGTH = 16;
    /**
     * 电子邮箱长度。
     */
    public static final int EMAIL_LENGTH = 1024;
    /**
     * 手机号长度。
     */
    public static final int PHONE_NUMBER_LENGTH = 20;
    /**
     * 个人简介长度。
     */
    public static final int USER_INTRODUCTION_LENGTH = 200;

    /**
     * SQL text 字段类型。
     */
    public static final String TEXT_COLUMN_TYPE = "text";
    /**
     * SQL 布尔字段默认假。
     */
    public static final String BOOL_COLUMN_FALSE = "bit(1) default 0";
    /**
     * SQL 布尔字段默认真。
     */
    public static final String BOOL_COLUMN_TRUE = "bit(1) default 1";

    /**
     * 菜单非重定向属性。
     * <p>
     * 非重定向值，表示在前端中，点击当前菜单的面包屑时，不具备跳转功能。
     */
    public static final String MENU_NO_REDIRECT = "noRedirect";

}
