package com.github.mrzhqiang.rowing.modules.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 密码修改表单。
 * <p>
 * 此表单用于修改密码，需要提供旧密码验证是否具备修改权限。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PasswordModifyForm extends PasswordConfirmForm {

    /**
     * 旧密码。
     * <p>
     * 用于验证是否具备修改权限，与 password 字段的校验规则及要求完全相同。
     */
    @JsonIgnore
    @ToString.Exclude
    @NotBlank
    @Size(min = 6, max = 15)
    private String oldPassword;
}
