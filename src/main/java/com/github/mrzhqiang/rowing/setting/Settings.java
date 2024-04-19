package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.attachment.AttachmentType;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.google.common.collect.ImmutableList;
import lombok.experimental.UtilityClass;
import org.springframework.util.ResourceUtils;
import org.springframework.util.unit.DataSize;

import java.time.Duration;
import java.util.List;

/**
 * 设置工具。
 */
@UtilityClass
public class Settings {

    /**
     * 系统设置 Excel 文件地址。
     */
    public static final String EXCEL_FILE_LOCATION = ResourceUtils.CLASSPATH_URL_PREFIX + "data/setting.xlsx";
    /**
     * 系统设置 Excel 文件中的 Sheet 页面名称。
     */
    public static final String EXCEL_FILE_SHEET_NAME = "data";

    /**
     * 网站标题。
     */
    public static final String TITLE = "title";
    /**
     * 网站欢迎文字。
     */
    public static final String WELCOME = "welcome";
    /**
     * 网站作者。
     */
    public static final String AUTHOR = "author";
    /**
     * 联系方式。
     */
    public static final String CONTACT = "contact";
    /**
     * 版权声明。
     */
    public static final String COPY_RIGHT = "copyRight";
    /**
     * 自动注册。
     */
    public static final String AUTO_REGISTER = "autoRegister";
    /**
     * 默认的自动注册。
     */
    public static final Logic DEF_AUTO_REGISTER = Logic.NO;
    /**
     * 最短密码长度。
     */
    public static final String MIN_PASSWORD_LENGTH = "minPasswordLength";
    /**
     * 默认的最短密码长度。
     */
    public static final int DEF_MIN_PASSWORD_LENGTH = 6;
    /**
     * 初始密码。
     */
    public static final String INIT_PASSWORD = "initPassword";
    /**
     * 默认的初始密码。
     */
    public static final String DEF_INIT_PASSWORD = "123456";
    /**
     * 密码有效期。
     */
    public static final String PASSWORD_EXPIRE = "passwordExpire";
    /**
     * 默认的密码有效期。
     */
    public static final Duration DEF_PASSWORD_EXPIRE = Duration.ofDays(180);
    /**
     * 附件大小。
     */
    public static final String ATTACHMENT_SIZE = "attachmentSize";
    /**
     * 默认的附件大小。
     */
    public static final DataSize DEF_ATTACHMENT_SIZE = DataSize.ofKilobytes(100);
    /**
     * 允许上传的附件类型。
     */
    public static final String ATTACHMENT_UPLOAD_WHITELIST = "attachmentUploadWhitelist";
    /**
     * 默认的允许上传附件类型。
     */
    public static final List<String> DEF_ATTACHMENT_UPLOAD_WHITELIST = ImmutableList.of(
            AttachmentType.JPEG.name().toLowerCase(),
            AttachmentType.PNG.name().toLowerCase(),
            AttachmentType.DOC.name().toLowerCase(),
            AttachmentType.DOCX.name().toLowerCase(),
            AttachmentType.XLS.name().toLowerCase(),
            AttachmentType.XLSX.name().toLowerCase(),
            AttachmentType.TXT.name().toLowerCase(),
            AttachmentType.CSV.name().toLowerCase(),
            AttachmentType.PDF.name().toLowerCase()
    );
    /**
     * 禁止上传的附件类型。
     */
    public static final String ATTACHMENT_UPLOAD_BLACKLIST = "attachmentUploadBlacklist";
    /**
     * 登录失败次数上限。
     */
    public static final String MAX_LOGIN_FAILED = "maxLoginFailed";
    /**
     * 默认的登录失败次数上限。
     */
    public static final int DEF_MAX_LOGIN_FAILED = 5;
    /**
     * 账户锁定持续时长。
     */
    public static final String ACCOUNT_LOCKED_DURATION = "accountLockedDuration";
    /**
     * 默认的账户锁定持续时长。
     */
    public static final Duration DEF_ACCOUNT_LOCKED_DURATION = Duration.ofMinutes(30);
    /**
     * 密码传输时的 RSA 私钥。
     */
    public static final String PASSWORD_RSA_PRIVATE_KEY = "passwordRsaPrivateKey";
    /**
     * 密码传输时的 RSA 公钥。
     */
    @SuppressWarnings("unused")
    public static final String PASSWORD_RSA_PUBLIC_KEY = "passwordRsaPublicKey";
    /**
     * 超级管理员电子邮箱。
     */
    public static final String SYSTEM_ADMIN_EMAIL = "adminEmail";
    /**
     * 超级管理员电话号码。
     */
    public static final String SYSTEM_ADMIN_PHONE_NUMBER = "adminPhoneNumber";
    /**
     * 管理员主页。
     */
    public static final String ADMIN_HOME_PATH = "adminHomePath";
    /**
     * 用户主页。
     */
    public static final String USER_HOME_PATH = "userHomePath";
    /**
     * 游客主页。
     */
    public static final String ANONYMOUS_HOME_PATH = "anonymousHomePath";
    /**
     * 管理员昵称前缀。
     */
    public static final String ADMIN_NICKNAME_PREFIX = "adminNicknamePrefix";
    /**
     * 默认的管理员昵称前缀。
     */
    public static final String DEF_ADMIN_NICKNAME_PREFIX = "管理";
    /**
     * 用户昵称前缀。
     */
    public static final String USER_NICKNAME_PREFIX = "userNicknamePrefix";
    /**
     * 默认的用户昵称前缀。
     */
    public static final String DEF_USER_NICKNAME_PREFIX = "用户";
    /**
     * 游客昵称前缀。
     */
    public static final String ANONYMOUS_NICKNAME_PREFIX = "anonymousNicknamePrefix";
    /**
     * 默认的游客昵称前缀。
     */
    public static final String DEF_ANONYMOUS_NICKNAME_PREFIX = "游客";

}
