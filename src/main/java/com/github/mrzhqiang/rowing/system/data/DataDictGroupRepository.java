package com.github.mrzhqiang.rowing.system.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataDictGroupRepository extends JpaRepository<DataDictGroup, Long> {

    void deleteAllByType(DataDictGroup.Type type);

    boolean existsByCode(String code);

    Optional<DataDictGroup> findByCode(String code);

    void deleteByCode(String code);
}
