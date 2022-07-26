package com.github.mrzhqiang.rowing.core.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统初始化检测器。
 * <p>
 * 主要检测 {@link AutoInitializer} 的实现类，是否已记录到数据库中。
 * <p>
 * 如果没有记录，则在系统启动时，自动记录到数据库，避免开发者创建后忘记添加。
 */
@Order(1)
@Component
public final class AutoInitializationChecker implements ApplicationRunner {

    private final SysInitMapper mapper;
    private final SysInitRepository repository;
    /**
     * 自动初始化实例列表。
     * <p>
     * Spring 支持直接注入接口的实现列表，也可以通过 ApplicationContext 的 getBeansOfType 方法获取接口实例的 Map 映射。
     * <p>
     * 这两种方法都可以实现需求，所以主要看使用的场景。
     * <p>
     * 如果是在 Spring Bean 的生命周期内，并且需要 AOP 等特性，那么就使用 Spring 的注入方式。
     * <p>
     * 如果是作为类似 Spring Security 框架的扩展实现，那么通过第二种获取方式会比较友好。
     * <p>
     * 说白了就是自己的应用中使用依赖注入方式，多方合作的模式中使用上下文获取方式。
     */
    private final List<AutoInitializer> autoInitializers;

    public AutoInitializationChecker(SysInitMapper mapper,
                                     SysInitRepository repository,
                                     List<AutoInitializer> autoInitializers) {
        this.mapper = mapper;
        this.repository = repository;
        this.autoInitializers = autoInitializers;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<SysInit> initList = autoInitializers.stream()
                .filter(it -> !repository.existsByName(it.getName()))
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(initList)) {
            return;
        }
        repository.saveAll(initList);
    }
}
