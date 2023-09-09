package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableExcerpt;
import org.springframework.data.rest.core.config.Projection;

/**
 * 国家地区代码摘要。
 */
@Projection(name = "dict-iso-3166-excerpt", types = {DictISO3166.class})
public interface DictISO3166Excerpt extends AuditableExcerpt {

    String getName();

    String getAlpha2Code();

    String getCnName();

    String getAlpha3Code();

    String getNumericCode();

}
