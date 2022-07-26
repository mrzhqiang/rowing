package com.github.mrzhqiang.rowing.core.system;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DataDictGroupData {

    @NotBlank
    private String name;
    @NotBlank
    private String code;
    @NotNull
    private DataDictGroup.Type type;

    private List<DataDictItemData> items;
}
