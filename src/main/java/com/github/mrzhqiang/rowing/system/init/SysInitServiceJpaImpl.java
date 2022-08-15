package com.github.mrzhqiang.rowing.system.init;

import com.github.mrzhqiang.rowing.system.AutoInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SysInitServiceJpaImpl implements SysInitService {

    private final SysInitMapper mapper;
    private final SysInitRepository repository;

    public SysInitServiceJpaImpl(SysInitMapper mapper,
                                 SysInitRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public List<SysInit> checkAndSave(List<AutoInitializer> initializers) {
        if (CollectionUtils.isEmpty(initializers)) {
            return Collections.emptyList();
        }

        List<SysInit> sysInits = initializers.stream()
                .filter(it -> !repository.existsByName(it.getName()))
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sysInits)) {
            return Collections.emptyList();
        }

        return repository.saveAll(sysInits);
    }
}
