package com.github.mrzhqiang.rowing.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.github.mrzhqiang.rowing.domain.Domains.USERNAME_MAX_LENGTH;
import static com.github.mrzhqiang.rowing.domain.Domains.USERNAME_MIN_LENGTH;

/**
 * 密码修改表单。
 * <p>
 * 此表单用于修改密码，需要提供旧密码验证是否具备修改权限。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PasswordModifyForm extends PasswordConfirmForm {

    /**
     * 旧密码。
     * <p>
     * 用于验证是否具备修改权限，与 password 字段的校验规则及要求完全相同。
     */
    @NotBlank
    @ToString.Exclude
    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH)
    private String oldPassword;

}
