package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * ISO 639 仓库。
 * <p>
 */
@RepositoryRestResource(path = "dict-iso-639")
public interface DictISO639Repository extends BaseRepository<DictISO639> {

}
