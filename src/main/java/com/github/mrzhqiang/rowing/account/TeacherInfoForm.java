package com.github.mrzhqiang.rowing.account;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 教师信息表单。
 * <p>
 */
@Data
public class TeacherInfoForm {

    /**
     * 教师编号。
     */
    @NotBlank
    private String number;
    /**
     * 姓名。
     */
    @NotBlank
    private String fullName;
    /**
     * 身份证。
     */
    @NotBlank
    private String idCard;

}
