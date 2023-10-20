package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.account.RunAsSystem;
import com.github.mrzhqiang.rowing.init.AutoInitializer;
import org.springframework.stereotype.Component;

/**
 * 考试初始化器。
 * <p>
 */
@Component
public class ExamAutoInitializer extends AutoInitializer {

    private final ExamService service;

    public ExamAutoInitializer(ExamService service) {
        this.service = service;
    }

    @Override
    protected void onExecute() {
        service.sync();
    }

}
