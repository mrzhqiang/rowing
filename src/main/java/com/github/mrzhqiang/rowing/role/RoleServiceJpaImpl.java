package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AccountType;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterCreateEvent;
import org.springframework.data.rest.core.event.BeforeCreateEvent;
import org.springframework.data.rest.webmvc.json.EnumTranslator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RoleServiceJpaImpl implements RoleService {

    private final RoleMapper mapper;
    private final RoleRepository repository;
    private final EnumTranslator translator;
    private final ApplicationEventPublisher eventPublisher;

    @Timed(longTask = true)
    @Counted
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void init() {
        Stream.of(AccountType.values()).forEach(this::createImmutableRole);
    }

    private void createImmutableRole(AccountType type) {
        Role role = mapper.toEntity(type, translator.asText(type));
        // 来自枚举的角色，设置为不可变，即内置角色
        role.setImmutable(true);
        eventPublisher.publishEvent(new BeforeCreateEvent(role));
        repository.save(role);
        eventPublisher.publishEvent(new AfterCreateEvent(role));
    }

    @Timed
    @Counted
    @Override
    public List<GrantedAuthority> findAuthoritiesBy(Account account) {
        if (account == null) {
            return Collections.emptyList();
        }
        return null;
    }

    @Timed
    @Counted
    @Override
    public void binding(Account account) {
        if (account == null) {
            return;
        }

        String roleCode = account.getType().getAuthority();
        // 通过账户类型找到对应的内置角色数据
        // 尝试将账户添加到角色的授权账户列表
        // 如果添加成功说明未重复，则保存角色实体数据
        repository.findByCodeAndImmutableIsTrue(roleCode)
                .filter(it -> it.getAccounts().add(account))
                .ifPresent(repository::save);
    }

    @Timed
    @Counted
    @Override
    public void addAccount(String roleCode, Account account) {
        repository.findByCode(roleCode)
                .filter(it -> it.getAccounts().add(account))
                .ifPresent(repository::save);
    }

    @Timed
    @Counted
    @Override
    public void removeAccount(String roleCode, Account account) {
        repository.findByCode(roleCode)
                .filter(it -> it.getAccounts().remove(account))
                .ifPresent(repository::save);
    }

}
