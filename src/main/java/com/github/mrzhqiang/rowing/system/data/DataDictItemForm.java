package com.github.mrzhqiang.rowing.system.data;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DataDictItemForm {

    private String icon;
    @NotBlank
    private String label;
    @NotBlank
    private String value;
}
