package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static com.github.mrzhqiang.rowing.domain.Domains.EMAIL_REGEXP;
import static com.github.mrzhqiang.rowing.domain.Domains.URL_LENGTH;

/**
 * 注册表单。
 * <p>
 * 这里是复杂的注册表单，如果是从第三方平台过来，或者想要简单的注册方式，也可以直接使用 {@link PasswordConfirmForm 密码确认表单}。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterForm extends PasswordConfirmForm {

    /**
     * 昵称。
     */
    @NotBlank
    @Size(max = Domains.MAX_USER_NICKNAME_LENGTH)
    private String nickname;
    /**
     * 头像。
     */
    @Size(max = URL_LENGTH)
    private String avatar;
    /**
     * 性别。
     */
    private Gender gender = Gender.UNKNOWN;
    /**
     * 生日。
     */
    private LocalDate birthday;
    /**
     * 电子邮箱。
     */
    @Size(max = Domains.EMAIL_LENGTH)
    @Email(regexp = EMAIL_REGEXP, message = "无效的邮箱")
    private String email;
    /**
     * 电话号码。
     */
    @Size(max = Domains.PHONE_NUMBER_LENGTH)
    private String phoneNumber;
    /**
     * 简介。
     */
    @Size(max = Domains.USER_INTRODUCTION_LENGTH)
    private String introduction;

}
