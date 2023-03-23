package com.github.mrzhqiang.rowing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import javax.annotation.Nonnull;

@Slf4j
@SpringBootApplication
public class RowingApplication implements ApplicationListener<ApplicationReadyEvent> {

    public static void main(String[] args) {
        SpringApplication.run(RowingApplication.class, args);
    }

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        log.info("=============================================");
        log.info("           系统启动完毕，可以正常使用！         ");
        log.info("=============================================");
    }
}
