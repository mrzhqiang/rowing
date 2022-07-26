package com.github.mrzhqiang.rowing.core.system;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class SysSettingForm {

    @NotBlank
    private String key;
    @NotBlank
    private String value;

    private List<SysSettingForm> children;
}
