package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.helper.Exceptions;
import lombok.experimental.UtilityClass;

/**
 * 领域工具。
 * <p>
 * 主要定义一些领域相关的常量和静态方法。
 */
@UtilityClass
public class Domains {

    /**
     * 枚举长度。
     * <p>
     * 对于领域字段来说，实际上是定义枚举的字典代码长度。
     */
    public static final int ENUM_LENGTH = 50;
    /**
     * 用户名最小长度。
     */
    public static final int USERNAME_MIN_LENGTH = 4;
    /**
     * 用户名最大长度。
     */
    public static final int USERNAME_MAX_LENGTH = 24;
    /**
     * 用户名正则表达式。
     * <p>
     * 规则：以字母开头，字母或数字结尾，允许 _ 符号以便追加第三方平台相关的前缀。
     * <p>
     * 注意：长度不在正则表达式的考虑范围内，应由表单验证进行控制。
     */
    public static final String USERNAME_REGEXP = "^[a-zA-Z][_a-zA-Z0-9]+";
    /**
     * 用户名注册时的正则表达式。
     * <p>
     * 规则：以小写字母开头，后面包含小写字母或数字，不允许任何特殊字符。
     * <p>
     * 注意：长度不在正则表达式的考虑范围内，应由表单验证进行控制。
     */
    public static final String USERNAME_REGISTER_REGEXP = "^[a-z][a-z0-9]+";
    /**
     * 密码最小长度。
     */
    public static final int PASSWORD_MIN_LENGTH = 6;
    /**
     * 默认的密码最大长度。
     * <p>
     * 仅限于注册时的密码长度限制，其他来源的密码长度没有明确限制，需要符合实体中的字段长度。
     * <p>
     * TODO 实际的密码长度限制应该从系统设置中读取。
     */
    public static final int DEF_PASSWORD_MAX_LENGTH = 32;
    /**
     * 权限信息长度。
     */
    public static final int AUTHORITIES_LENGTH = 2000;
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
     * 字典组名称长度。
     */
    public static final int DICT_GROUP_NAME_LENGTH = 100;
    /**
     * 字典组代码长度。
     */
    public static final int DICT_GROUP_CODE_LENGTH = 100;
    /**
     * 字典项标签长度。
     */
    public static final int DICT_ITEM_LABEL_LENGTH = 100;
    /**
     * 字典项值长度。
     */
    public static final int DICT_ITEM_VALUE_LENGTH = ENUM_LENGTH;
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
    public static final int EXCEPTION_TRACE_LENGTH = 2000;
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
    public static final int ROLE_CODE_LENGTH = 100;
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
     * 最小用户昵称生成长度。
     */
    public static final int MIN_NICKNAME_GENERATE_LENGTH = 6;
    /**
     * 用户昵称最大长度。
     */
    public static final int MAX_USER_NICKNAME_LENGTH = 16;
    /**
     * URL 地址的正则表达式。
     */
    public static final String URL_REGEXP = "^(((ht|f)tps?)://)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$";
    /**
     * URL 地址的长度限制。
     */
    public static final int URL_LENGTH = 8 * 1024;
    /**
     * 电子邮箱地址的正则表达式。
     */
    public static final String EMAIL_REGEXP = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    /**
     * 电子邮箱长度。
     */
    public static final int EMAIL_LENGTH = 1024;
    /**
     * 手机号长度。
     */
    public static final int PHONE_NUMBER_LENGTH = 20;
    /**
     * 手机号正则表达式——宽松模式。
     */
    public static final String PHONE_NUMBER_REGEXP = "(?:(?:\\+|00)86)?1\\d{10}$";
    /**
     * 手机号正则表达式——严谨模式。
     */
    public static final String PHONE_NUMBER_REGEXP_STRICT = "^(?:(?:\\+|00)86)?1[3-9]\\d{9}$";
    /**
     * 手机号正则表达式——最严格模式。
     */
    public static final String PHONE_NUMBER_REGEXP_STRICTEST = "^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[1|8|9]))\\d{8}$";
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
     * 资源名称最大长度。
     */
    public static final int MAX_NAME_LENGTH = 24;
    /**
     * 资源权限最大长度。
     */
    public static final int MAX_AUTHORITY_LENGTH = 100;

}
