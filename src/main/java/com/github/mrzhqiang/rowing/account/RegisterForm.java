package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static com.github.mrzhqiang.rowing.domain.Domains.*;

/**
 * 注册表单。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterForm extends PasswordConfirmForm {

    /**
     * 昵称。
     */
    @NotBlank
    @Size(max = Domains.USER_NICKNAME_LENGTH, message = "昵称长度超过限制")
    private String nickname;
    /**
     * 头像。
     */
    @Size(max = URL_LENGTH, message = "头像长度超过限制")
    @Pattern(regexp = URL_REGEXP, message = "无效的头像")
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
    @Size(max = Domains.EMAIL_LENGTH, message = "邮箱长度超过限制")
    @Email(regexp = EMAIL_REGEXP, message = "无效的邮箱")
    private String email;
    /**
     * 电话号码。
     */
    @Size(max = Domains.PHONE_NUMBER_LENGTH, message = "手机号长度超过限制")
    @Pattern(regexp = PHONE_NUMBER_REGEXP, message = "无效的手机号")
    private String phoneNumber;
    /**
     * 简介。
     */
    @Size(max = Domains.USER_INTRODUCTION_LENGTH, message = "简介长度超过限制")
    private String introduction;

}
