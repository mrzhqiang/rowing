package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableExcerpt;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * 字典表单。
 */
@Projection(name = "dict-form", types = {DictGroup.class})
public interface DictForm extends AuditableExcerpt {

    String getName();

    String getCode();

    List<DictItemForm> getItems();

}
