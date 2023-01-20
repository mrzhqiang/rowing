package com.github.mrzhqiang.rowing.modules.action;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}
