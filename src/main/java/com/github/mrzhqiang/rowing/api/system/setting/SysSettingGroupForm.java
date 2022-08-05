package com.github.mrzhqiang.rowing.api.system.setting;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysSettingGroupForm {

    @NotBlank
    private String name;
    @NotBlank
    private String code;
}
