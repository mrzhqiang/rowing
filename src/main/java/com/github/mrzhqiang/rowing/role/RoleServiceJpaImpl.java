package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AccountType;
import org.springframework.data.rest.webmvc.json.EnumTranslator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        // 来自枚举的角色，需要设置为不可变
        role.setImmutable(true);
        repository.save(role);
    }

    @Override
    public void bindingAccount(Account account) {
        if (account == null) {
            return;
        }

        String role = account.getType().name();
        repository.findByCode(role).ifPresent(it -> {
            List<Role> roleList = account.getRoleList();
            if (roleList.contains(it)) {
                return;
            }
            // account 是角色的拥有方，通过它添加 role 并保存比较合适
            // 如果在 role 中添加 account 并保存，不会正确建立关联关系
            roleList.add(it);
        });
    }

}
