package com.github.mrzhqiang.rowing.modules.account;

import lombok.Data;
import lombok.ToString;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 登录表单。
 * <p>
 * 表示通过用户名和密码即可登录系统。
 */
@Data
public class LoginForm {

    /**
     * 用户名。
     * <p>
     * 不能为空值或空串。
     * <p>
     * 长度最小 4 位，最大 20 位。
     * <p>
     * Pattern 注解的 regexp 参数是正则表达式，message 参数则用于校验失败时返回的错误消息，前后包含 '{' 和 '}' 字符则说明是国际化消息。
     * <p>
     * 注意：{@link LocalValidatorFactoryBean} 已为 {@link Validation} 默认绑定国际化消息，因此项目中不用再配置 Bean 声明。
     */
    @NotBlank
    @Size(min = 4, max = 24)
    @Pattern(regexp = "^[a-z]+[a-z\\d]*$", message = "{LoginForm.username}")
    private String username;
    /**
     * 密码。
     * <p>
     * 不能为空值或空串。
     * <p>
     * 长度最小 6 位，最大 32 位。
     * <p>
     * 这里通常是明文密码，前端不做编码处理。
     * <p>
     * 另外，在 {@link #toString()} 时，需要忽略密码字段，避免通过日志泄漏明文密码。
     * <p>
     * 而 Jsons 工具类已配置忽略此字段，其他对象的序列化则使用注解自行把控。
     */
    @ToString.Exclude
    @NotBlank
    @Size(min = 6, max = 32)
    private String password;
}
