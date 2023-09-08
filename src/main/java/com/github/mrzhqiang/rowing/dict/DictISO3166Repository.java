package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * ISO 3166 仓库。
 * <p>
 */
@RepositoryRestResource(path = "dict-iso-3166")
public interface DictISO3166Repository extends BaseRepository<DictISO3166> {

}
