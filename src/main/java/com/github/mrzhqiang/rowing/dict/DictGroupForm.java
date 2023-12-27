package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * 字典表单。
 */
@Projection(name = "dict-form", types = {DictGroup.class})
public interface DictGroupForm extends BaseProjection {

    String getName();

    String getCode();

    List<DictItemExcerpt> getItems();

}
