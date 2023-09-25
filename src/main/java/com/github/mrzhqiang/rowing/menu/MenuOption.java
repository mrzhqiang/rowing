package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * 菜单选项。
 * <p>
 * 提供给前端的下拉选择框、级联选择框等组件使用。
 */
@Projection(name = "menu-option", types = {Menu.class})
public interface MenuOption extends BaseProjection {

    @Value("#{target.title}")
    String getLabel();

    String getFullPath();

    @Value("#{target.path?.startsWith('http')}")
    Boolean getIsDisabled();

    List<MenuOption> getChildren();

}
