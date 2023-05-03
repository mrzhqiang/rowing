package com.github.mrzhqiang.rowing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import java.time.temporal.ChronoUnit;

/**
 * 行舟在线考试系统。
 */
@Slf4j
@SpringBootApplication
public class RowingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RowingApplication.class, args);
    }

    @Order
    @EventListener
    public void onEvent(ApplicationReadyEvent event) {
        log.info("===========================================");
        String time = DurationStyle.SIMPLE.print(event.getTimeTaken(), ChronoUnit.SECONDS);
        log.info("系统启动完毕，用时 {}，现在可以正常使用！", time);
        log.info("===========================================");
    }
}
