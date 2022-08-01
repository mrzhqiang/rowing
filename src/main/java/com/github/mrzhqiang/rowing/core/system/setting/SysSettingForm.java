package com.github.mrzhqiang.rowing.core.system.setting;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class SysSettingForm {

    @NotBlank
    private String name;
    @NotBlank
    private String value;

    private List<SysSettingForm> children;
}
