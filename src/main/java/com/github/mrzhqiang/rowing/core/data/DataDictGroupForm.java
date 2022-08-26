package com.github.mrzhqiang.rowing.core.data;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 字典组表单。
 * <p>
 * 通过此表单生成的字典组实体，一律是自定义类型。
 */
@Data
public class DataDictGroupForm {

    @NotBlank
    private String name;
    @NotBlank
    private String code;
}
