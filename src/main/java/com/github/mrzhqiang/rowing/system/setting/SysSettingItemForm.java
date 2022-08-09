package com.github.mrzhqiang.rowing.system.setting;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysSettingItemForm {

    @NotBlank
    private String label;
    @NotBlank
    private String name;
    @NotBlank
    private String value;
}
