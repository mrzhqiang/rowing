package com.github.mrzhqiang.rowing.core.data;

import com.github.mrzhqiang.rowing.core.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * 数据字典项仓库。
 */
@RepositoryRestResource(exported = false)
public interface DataDictItemRepository extends BaseRepository<DataDictItem> {

}
