package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableExcerpt;
import org.springframework.data.rest.core.config.Projection;

/**
 * 国家地区代码表单。
 */
@Projection(name = "dict-iso-3166-form", types = {DictISO3166.class})
public interface DictISO3166Form extends AuditableExcerpt {

    String getName();

    String getAlpha2Code();

    String getCnName();

    String getAlpha3Code();

    String getNumericCode();

}
