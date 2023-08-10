package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.menu.Menu;
import org.springframework.data.rest.webmvc.json.EnumTranslator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Stream;

/**
 * 角色服务的 JPA 实现。
 * <p>
 */
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindingMenu(String roleCode, Menu menu) {
        if (StringUtils.hasText(roleCode) && menu != null) {
            repository.findByCode(roleCode).ifPresent(it -> {
                it.getMenuList().add(menu);
                repository.save(it);
            });
        }
    }
}
