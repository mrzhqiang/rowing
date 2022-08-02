package com.github.mrzhqiang.rowing.api.system.setting;

import lombok.Data;

import java.util.List;

@Data
public class SysSettingData {

    private String name;
    private String value;
    private List<SysSettingData> children;
}
