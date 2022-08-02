package com.github.mrzhqiang.rowing.api.system.exception;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExceptionLogRepository extends JpaRepository<ExceptionLog, Long> {

}
