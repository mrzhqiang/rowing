package com.github.mrzhqiang.rowing.core.system;

import com.github.mrzhqiang.rowing.core.system.init.SysInit;
import com.github.mrzhqiang.rowing.core.system.init.SysInitService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * 自动初始化检测运行器。
 * <p>
 * 主要检测 {@link AutoInitializer} 的实现类，是否已记录到数据库中。
 * <p>
 * 如果没有记录，则在系统启动时，自动记录到数据库，避免开发者创建后忘记添加。
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class AutoInitializationCheckRunner implements ApplicationRunner, ApplicationContextAware {

    private final SysInitService service;
    private ApplicationContext context;

    public AutoInitializationCheckRunner(SysInitService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {
        /*
         * Spring 支持直接注入接口的实现列表，也可以通过 ApplicationContext 的 getBeansOfType 方法获取接口实现的 Map 映射。
         * 这两种方法都可以实现需求，因此主要看使用的场景：
         * 1. 如果列表的持有类参与 Spring Bean 的生命周期，比如持有类被声明为 Component 组件，建议使用 Spring 的构造器注入。
         * 2. 如果相关类未参与 Spring Bean 的生命周期，比如在静态方法中处理列表，建议通过 getBeansOfType 方法获取列表。
         * 但一个例外情况就是，如果相关类参与了部分的生命周期，而不是完整的生命周期，那么使用第二种方法可以减少内存占用。
         */
        Map<String, AutoInitializer> initializerMap = context.getBeansOfType(AutoInitializer.class);
        List<AutoInitializer> initializers = Lists.newArrayList(initializerMap.values());
        AnnotationAwareOrderComparator.sort(initializers);
        List<SysInit> check = service.check(initializers);
        if (check.isEmpty()) {
            log.info("检测完毕！未发现有新的自动初始化器");
        } else {
            log.info("检测完毕！发现有 {} 个新的自动初始化器，已记录到数据库", check.size());
        }
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
