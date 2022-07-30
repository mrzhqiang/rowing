package com.github.mrzhqiang.rowing.core.system.data;

import lombok.Data;

import java.util.List;

@Data
public class DataDictGroupData {

    private String name;
    private String code;
    private String type;

    private List<DataDictItemData> items;
}
