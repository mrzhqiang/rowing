package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 语言代码摘要。
 */
@Projection(name = "dict-iso-639-excerpt", types = {DictISO639.class})
public interface DictISO639Excerpt extends AuditableProjection {

    String getFamily();

    String getCnName();

    String getSelfName();

    String getName();

    String getCode();

    String getNotes();

}
