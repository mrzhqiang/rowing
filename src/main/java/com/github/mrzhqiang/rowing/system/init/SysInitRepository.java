package com.github.mrzhqiang.rowing.system.init;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysInitRepository extends JpaRepository<SysInit, Long> {

    boolean existsByName(String name);

    Optional<SysInit> findByName(String name);
}
