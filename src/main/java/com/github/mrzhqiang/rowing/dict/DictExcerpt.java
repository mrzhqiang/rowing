package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableExcerpt;
import com.github.mrzhqiang.rowing.domain.DictType;
import com.github.mrzhqiang.rowing.domain.Logic;
import org.springframework.data.rest.core.config.Projection;

/**
 * 字典摘要。
 * <p>
 * 字典本身的字段都适合放进管理页面的列表，但是字典项字段缺失了，因为字典项拥有 Repository 仓库，所以它在字典组内将是一个 Link 链接。
 * <p>
 * 为了解决以上问题，字典组需要一个字典摘要。
 */
@Projection(name = "dict-excerpt", types = {DictGroup.class})
public interface DictExcerpt extends AuditableExcerpt {

    String getName();

    String getCode();

    DictType getType();

    Logic getFreeze();

}
