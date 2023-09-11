package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 语言代码表单。
 */
@Projection(name = "dict-iso-639-form", types = {DictISO639.class})
public interface DictISO639Form extends AuditableProjection {

    String getName();

    String getCode();

    String getCnName();

    String getNotes();

}
