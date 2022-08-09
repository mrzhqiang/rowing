package com.github.mrzhqiang.rowing.system.setting;

import lombok.Data;

import java.util.List;

@Data
public class SysSettingGroupData {

    private String id;
    private String name;
    private List<SysSettingItemData> items;
}
