package com.github.mrzhqiang.rowing.core.data;

import com.github.mrzhqiang.rowing.core.domain.BaseRepository;

import java.util.Optional;

public interface DataDictGroupRepository extends BaseRepository<DataDictGroup> {

    boolean existsByCode(String code);

    Optional<DataDictGroup> findByCode(String code);
}
