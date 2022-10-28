package com.github.mrzhqiang.rowing.system.init;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统初始化服务的 JPA 实现。
 */
@Service
@Transactional
public class SysInitServiceJpaImpl implements SysInitService {

    private final SysInitMapper mapper;
    private final SysInitRepository repository;
    private final List<AutoInitializer> initializers;

    public SysInitServiceJpaImpl(SysInitMapper mapper,
                                 SysInitRepository repository,
                                 List<AutoInitializer> initializers) {
        this.initializers = initializers;
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public List<SysInit> syncAutoInitializer() {
        if (CollectionUtils.isEmpty(initializers)) {
            return Collections.emptyList();
        }

        return initializers.stream()
                .filter(it -> !repository.existsByPath(it.getPath()))
                .map(mapper::toEntity)
                .map(repository::save)
                .collect(Collectors.toList());
    }
}
