package com.github.mrzhqiang.rowing.account;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 学生表单。
 * <p>
 * 用来建立学生账户和系统账户的表单。
 */
@Data
public class StudentInfoForm {

    /**
     * 学号。
     * <p>
     * 不能为空。
     */
    @NotBlank
    private String number;
    /**
     * 姓名。
     * <p>
     * 不能为空。
     */
    @NotBlank
    private String fullName;
    /**
     * 身份证。
     * <p>
     * 不能为空。
     */
    @NotBlank
    private String idCard;
}
