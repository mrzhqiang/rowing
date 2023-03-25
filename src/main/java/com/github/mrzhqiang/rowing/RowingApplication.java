package com.github.mrzhqiang.rowing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
public class RowingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RowingApplication.class, args);
    }

    @EventListener
    public void onEvent(ApplicationReadyEvent event) {
        log.info("=============================================");
        log.info("     系统启动完毕，用时 {}，现在可以正常使用！    ", event.getTimeTaken());
        log.info("=============================================");
    }

    @EventListener
    public void onEvent(ApplicationFailedEvent event) {
        log.error("=============================================");
        log.error("           系统启动失败，请检查错误：           ", event.getException());
        log.error("=============================================");
    }
}
