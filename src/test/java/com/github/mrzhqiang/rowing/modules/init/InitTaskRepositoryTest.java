package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.domain.TaskType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * 初始化任务仓库测试类。
 * <p>
 */
@SpringBootTest
class InitTaskRepositoryTest {

    @Autowired
    private InitTaskRepository repository;

    @WithMockUser(username = "test", password = "123456", roles = {"ADMIN"})
    @Test
    void testSave() {
        InitTask initTask = new InitTask();
        initTask.setPath(InitTask.class.getName());
        initTask.setName(InitTask.class.getSimpleName());
        initTask.setType(TaskType.SYSTEM);
        initTask.setStatus(TaskStatus.DEFAULT);
        initTask.setOrdered(1);
        initTask.setDiscard(Logic.NO);
        repository.save(initTask);
    }
}
