package com.github.mrzhqiang.rowing.dict.iso;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 国家地区代码摘要。
 */
@Projection(name = "dict-iso-3166-excerpt", types = {DictISO3166.class})
public interface DictISO3166Excerpt extends AuditableProjection {

    String getName();

    String getAlpha2Code();

    String getCnName();

    String getAlpha3Code();

    String getNumericCode();

}
