package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.init.AutoInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 考试初始化器。
 * <p>
 */
@Component
@RequiredArgsConstructor
public class ExamAutoInitializer extends AutoInitializer {

    private final ExamService service;

    @Override
    protected void onExecute() {
        service.sync();
    }

}
