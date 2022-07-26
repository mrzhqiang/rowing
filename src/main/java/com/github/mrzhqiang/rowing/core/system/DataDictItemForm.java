package com.github.mrzhqiang.rowing.core.system;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DataDictItemForm {

    @NotBlank
    private String label;
    @NotBlank
    private String value;
}
