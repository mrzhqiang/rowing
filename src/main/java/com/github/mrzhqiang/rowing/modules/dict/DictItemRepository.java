package com.github.mrzhqiang.rowing.modules.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * 数据字典项仓库。
 */
@RepositoryRestResource(exported = false)
public interface DictItemRepository extends BaseRepository<DictItem> {

}
