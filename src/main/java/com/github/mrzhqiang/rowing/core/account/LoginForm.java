package com.github.mrzhqiang.rowing.core.account;

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
     * message 用于校验失败时返回的错误消息，前后包含 '{' 和 '}' 字符则说明是国际化消息。
     * <p>
     * {@link LocalValidatorFactoryBean} 已为 {@link Validation} 默认绑定国际化消息，因此不必再配置 Bean 声明。
     */
    @NotBlank
    @Size(min = 4, max = 15)
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z\\d]*$", message = "{LoginForm.username}")
    private String username;
    /**
     * 密码。
     * <p>
     * 校验规则：
     * <p>
     * 1. 不能为 null 且至少包含一个非空白字符。
     * <p>
     * 2. 长度最小 6 位，最大 15 位。
     * <p>
     * 这里通常是明文密码，前端不做编码处理。
     * <p>
     * 另外，在 toString 时，必须忽略密码字段。
     * <p>
     * Json 序列化已跳过配置项忽略此字段。
     */
    @ToString.Exclude
    @NotBlank
    @Size(min = 6, max = 15)
    private String password;
}
