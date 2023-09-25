package com.github.mrzhqiang.rowing.dict.iso;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 语言代码表单。
 */
@Projection(name = "dict-iso-639-form", types = {DictISO639.class})
public interface DictISO639Form extends BaseProjection {

    String getName();

    String getCode();

    String getCnName();

    String getNotes();

}
