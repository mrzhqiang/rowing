package com.github.mrzhqiang.rowing;

import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

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
        Duration duration = event.getTimeTaken();
        String time = DurationStyle.SIMPLE.print(duration, SECONDS);
        String message = I18nHolder.getAccessor().getMessage(
                "RowingApplication.onEvent.message", new Object[]{time},
                Strings.lenientFormat("系统启动完毕，用时：%s，现在可以正常使用！", time));
        log.info("===========================================");
        log.info(message);
        log.info("===========================================");
    }

}
