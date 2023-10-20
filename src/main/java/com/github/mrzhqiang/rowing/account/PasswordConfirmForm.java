package com.github.mrzhqiang.rowing.account;

import lombok.Data;
import lombok.ToString;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

import static com.github.mrzhqiang.rowing.domain.Domains.*;

/**
 * 密码确认表单。
 * <p>
 * 此表单一般用于二次确认密码是否一致，避免用户输入出错。
 */
@Data
public class PasswordConfirmForm {

    /**
     * 用户名。
     * <p>
     * Pattern 注解的 regexp 参数是正则表达式，message 参数则用于校验失败时返回的错误消息，前后包含 '{' 和 '}' 字符则说明是国际化消息。
     * <p>
     * 注意：{@link LocalValidatorFactoryBean} 已为 {@link Validation} 默认绑定国际化消息，因此项目中不用再配置 Bean 声明。
     */
    @NotBlank
    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH)
    @Pattern(regexp = USERNAME_REGEXP, message = "无效的用户名，必须以小写字母开头，后面包含小写字母或数字")
    private String username;
    /**
     * 密码。
     * <p>
     * 这里通常是明文密码，前端不做编码处理。
     * <p>
     * 另外，在 {@link #toString()} 时，需要忽略密码字段，避免日志泄漏明文密码。
     */
    @ToString.Exclude
    @NotBlank
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;
    /**
     * 确认密码。
     * <p>
     * 用于比较是否与要修改的密码完全一致，与 password 字段的校验规则及要求完全相同。
     */
    @ToString.Exclude
    @NotBlank
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String confirmPassword;

    /**
     * 确认密码。
     * <p>
     * 用来验证两次输入的密码是否一致，如果不一致，则提醒用户检测输入值。
     *
     * @return 返回 True 表示两次密码完全一致；返回 False 则相反。
     */
    public boolean confirm() {
        return Objects.equals(password, confirmPassword);
    }

}
