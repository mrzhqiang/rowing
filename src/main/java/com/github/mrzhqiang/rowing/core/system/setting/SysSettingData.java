package com.github.mrzhqiang.rowing.core.system.setting;

import lombok.Data;

import java.util.List;

@Data
public class SysSettingData {

    private String key;
    private String value;
    private List<SysSettingData> children;
}
