package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 设置表单。
 */
@Projection(name = "setting-form", types = {Setting.class})
public interface SettingForm extends BaseProjection {

    String getType();

    String getName();

    String getCode();

    String getContent();

}
