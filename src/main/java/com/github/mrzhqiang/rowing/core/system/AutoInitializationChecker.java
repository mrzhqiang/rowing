package com.github.mrzhqiang.rowing.core.system;

import com.github.mrzhqiang.rowing.core.system.init.SysInit;
import com.github.mrzhqiang.rowing.core.system.init.SysInitMapper;
import com.github.mrzhqiang.rowing.core.system.init.SysInitRepository;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
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
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public final class AutoInitializationChecker implements ApplicationRunner {

    private final SysInitMapper mapper;
    private final SysInitRepository repository;
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

    public AutoInitializationChecker(SysInitMapper mapper,
                                     SysInitRepository repository,
                                     List<AutoInitializer> autoInitializers) {
        this.mapper = mapper;
        this.repository = repository;
        this.autoInitializers = autoInitializers;
    }

    @Override
    public void run(ApplicationArguments args) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        log.info("准备检测：是否存在未记录的自动初始化器");
        List<SysInit> initList = autoInitializers.stream()
                .filter(it -> !repository.existsByName(it.getName()))
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(initList)) {
            log.info("检测完毕！不存在未记录的自动初始化器，检测耗时：{}", stopwatch.stop());
            return;
        }
        log.info("检测到有 {} 个未记录的自动初始化器，准备记录到数据库", initList.size());
        repository.saveAll(initList);
        log.info("记录完毕！所有未记录的自动初始化器已记录到数据库，检测并记录共耗时：{}", stopwatch.stop());
    }
}
