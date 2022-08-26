package com.github.mrzhqiang.rowing.core.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * 密码确认表单。
 * <p>
 * 此表单一般用于修改密码时的二次确认。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PasswordConfirmForm extends LoginForm {

    /**
     * 确认密码。
     * <p>
     * 用于比较是否与要修改的密码完全一致，与 password 字段的校验规则及要求完全相同。
     */
    @JsonIgnore
    @ToString.Exclude
    @NotBlank
    @Size(min = 6, max = 15)
    private String confirmPassword;

    /**
     * 确认密码。
     * <p>
     * 用来验证两次输入的密码是否一致，如果不一致，则提醒用户检测输入值。
     *
     * @return True 表示两次密码完全一致；False 则表示两次密码完全不同。
     */
    public boolean confirm() {
        return Objects.equals(getPassword(), confirmPassword);
    }
}
