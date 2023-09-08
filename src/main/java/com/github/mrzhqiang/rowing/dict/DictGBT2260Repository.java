package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * GB/T 2260 标准仓库。
 * <p>
 */
@RepositoryRestResource(path = "dict-gbt-2260")
public interface DictGBT2260Repository extends BaseRepository<DictGBT2260> {

}
