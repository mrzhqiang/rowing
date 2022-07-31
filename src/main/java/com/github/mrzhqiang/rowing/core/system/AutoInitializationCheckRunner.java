package com.github.mrzhqiang.rowing.core.system;

import com.github.mrzhqiang.rowing.core.system.init.SysInit;
import com.github.mrzhqiang.rowing.core.system.init.SysInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自动初始化检测运行器。
 * <p>
 * 主要检测 {@link AutoInitializer} 的实现类，是否已记录到数据库中。
 * <p>
 * 如果没有记录，则在系统启动时，自动记录到数据库，避免开发者创建后忘记添加。
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public final class AutoInitializationCheckRunner implements ApplicationRunner {

    private final SysInitService service;
    /**
     * 自动初始化实例列表。
     * <p>
     * Spring 支持直接注入接口的实现列表，也可以通过 ApplicationContext 的 getBeansOfType 方法获取接口实例的 Map 映射。
     * <p>
     * 这两种方法都可以实现需求，因此主要看使用的场景。
     * <p>
     * 如果列表的持有类参与 Spring Bean 的生命周期，比如持有类被声明为 Component 组件，建议使用 Spring 的构造器注入。
     * <p>
     * 如果相关类未参与 Spring Bean 的生命周期，比如在静态方法中处理列表，建议通过 getBeansOfType 方法获取列表。
     */
    private final List<AutoInitializer> autoInitializers;

    public AutoInitializationCheckRunner(SysInitService service,
                                         List<AutoInitializer> autoInitializers) {
        this.service = service;
        this.autoInitializers = autoInitializers;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<SysInit> check = service.check(autoInitializers);
        log.info("检测完毕！发现 {} 个新的自动初始化器，已记录到数据库", check.size());
    }
}
