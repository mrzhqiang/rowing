package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.domain.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

/**
 * 初始化任务仓库测试类。
 * <p>
 */
@RunWith(SpringRunner.class)
@DataJpaTest
class InitTaskRepositoryTest {

    @Autowired
    private InitTaskRepository repository;

    @Test
    void testCrud() {
        String path = InitTask.class.getName();
        String name = InitTask.class.getSimpleName();

        InitTask initTask = new InitTask();
        initTask.setPath(path);
        initTask.setName(name);
        initTask.setType(TaskType.SYSTEM);
        initTask.setStatus(TaskStatus.DEFAULT);
        initTask.setOrdered(1);
        initTask.setDiscard(Logic.NO);
        repository.save(initTask);
        Assertions.assertTrue(Objects.nonNull(initTask.getId()));

        InitTask findTask = repository.findByPath(path);
        Assertions.assertEquals(initTask, findTask);

        Assertions.assertTrue(repository.findAll().contains(initTask));

        repository.delete(initTask);
        Assertions.assertFalse(repository.existsByPath(path));
        Assertions.assertEquals(0, repository.count());
    }

    @Test
    void testUnique() {
        String path = InitTask.class.getName();
        String name = InitTask.class.getSimpleName();

        InitTask initTask = new InitTask();
        initTask.setPath(path);
        initTask.setName(name);
        initTask.setType(TaskType.SYSTEM);
        initTask.setStatus(TaskStatus.DEFAULT);
        initTask.setOrdered(1);
        initTask.setDiscard(Logic.NO);
        repository.save(initTask);

        InitTask repeatInitTask = new InitTask();
        repeatInitTask.setPath(path);
        repeatInitTask.setName(name);
        repeatInitTask.setType(TaskType.SYSTEM);
        repeatInitTask.setStatus(TaskStatus.DEFAULT);
        repeatInitTask.setOrdered(1);
        repeatInitTask.setDiscard(Logic.NO);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(repeatInitTask));
    }
}
