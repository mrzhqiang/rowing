package com.github.mrzhqiang.rowing.system.init;

import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.system.AutoInitializer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysInitServiceJpaImpl implements SysInitService {

    private final SysInitMapper mapper;
    private final SysInitRepository repository;

    public SysInitServiceJpaImpl(SysInitMapper mapper,
                                 SysInitRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public List<SysInit> check(List<AutoInitializer> initializers) {
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

    @Override
    public boolean checkFinishedBy(String name) {
        return repository.findByName(name)
                .map(SysInit::hasFinished)
                .orElse(false);
    }

    @Override
    public void updateFinishedBy(String name) {
        repository.findByName(name).ifPresent(it -> {
            it.setStatus(TaskStatus.FINISHED);
            repository.save(it);
        });
    }
}
