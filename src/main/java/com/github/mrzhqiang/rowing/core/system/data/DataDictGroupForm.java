package com.github.mrzhqiang.rowing.core.system.data;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DataDictGroupForm {

    @NotBlank
    private String name;
    @NotBlank
    private String code;
    @NotNull
    private DataDictGroup.Type type;
}
