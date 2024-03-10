package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.helper.text.CommonSymbols;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AccountType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.springframework.data.rest.webmvc.json.EnumTranslator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoleServiceJpaImpl implements RoleService {

    private final RoleMapper mapper;
    private final RoleRepository repository;
    private final EnumTranslator translator;

    public RoleServiceJpaImpl(RoleMapper mapper,
                              RoleRepository repository,
                              EnumTranslator translator) {
        this.mapper = mapper;
        this.repository = repository;
        this.translator = translator;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void init() {
        Stream.of(AccountType.values()).forEach(this::createImmutableRole);
    }

    private void createImmutableRole(AccountType type) {
        Role role = mapper.toEntity(type, translator.asText(type));
        // 来自枚举的角色，设置为不可变，即内置角色
        role.setImmutable(true);
        repository.save(role);
    }

    @Override
    public void binding(Account account) {
        if (account == null) {
            return;
        }

    }

    @Override
    public List<Role> findAllBy(Account account) {
        return repository.findAllByAccounts(ImmutableList.of(account));
    }

}
