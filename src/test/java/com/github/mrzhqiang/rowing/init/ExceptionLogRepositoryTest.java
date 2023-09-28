package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.exception.ExceptionLog;
import com.github.mrzhqiang.rowing.exception.ExceptionLogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
class ExceptionLogRepositoryTest {

    @Autowired
    private ExceptionLogRepository repository;

    @Test
    void testCrud() {
        ExceptionLog log = new ExceptionLog();
        log.setStatus(HttpStatus.NOT_FOUND.value());
        log.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        repository.save(log);
        Assertions.assertTrue(Objects.nonNull(log.getId()));

        Optional<ExceptionLog> logOptional = repository.findById(log.getId());
        Assertions.assertTrue(logOptional.isPresent());
        Assertions.assertEquals(log, logOptional.get());

        Assertions.assertTrue(repository.findAll().contains(log));

        repository.delete(log);
        Assertions.assertFalse(repository.existsById(log.getId()));
        Assertions.assertEquals(0, repository.count());
    }

}
