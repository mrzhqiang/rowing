package com.github.mrzhqiang.rowing.system.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataDictGroupRepository extends JpaRepository<DataDictGroup, Long> {

    boolean existsByCode(String code);

    Optional<DataDictGroup> findByCode(String code);

    void deleteByCode(String code);
}
