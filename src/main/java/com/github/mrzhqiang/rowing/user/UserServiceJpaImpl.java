package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.Gender;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.menu.MenuData;
import com.github.mrzhqiang.rowing.menu.MenuMapper;
import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceJpaImpl implements UserService {

    private final UserMapper mapper;
    private final MenuMapper menuMapper;
    private final UserRepository repository;
    private final SettingService settingService;

    public UserServiceJpaImpl(UserMapper mapper,
                              MenuMapper menuMapper,
                              UserRepository repository,
                              SettingService settingService) {
        this.mapper = mapper;
        this.menuMapper = menuMapper;
        this.repository = repository;
        this.settingService = settingService;
    }

    @Override
    public UserInfoData findByUsername(String username) {
        return repository.findByOwner_Username(username)
                .map(this::convertToData)
                .orElse(null);
    }

    private UserInfoData convertToData(User user) {
        UserInfoData data = mapper.toData(user);
        data.setRoles(user.getOwner().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return data;
    }

    @Override
    public void bindingAdmin(Account admin) {
        repository.findByOwner(admin).orElseGet(() -> createAdminUser(admin));
    }

    private User createAdminUser(Account admin) {
        User user = User.builder()
                .nickname(I18nHolder.getAccessor().getMessage(
                        "AccountService.nickname.admin", "管理员"))
                .gender(Gender.MALE)
                .birthday(LocalDate.of(2019, Month.of(10), 25))
                .email(settingService.findByName(SettingService.ADMIN_EMAIL)
                        .map(Setting::getContent)
                        .orElse(""))
                .phoneNumber(settingService.findByName(SettingService.ADMIN_PHONE_NUMBER)
                        .map(Setting::getContent)
                        .orElse(""))
                .introduction(I18nHolder.getAccessor().getMessage(
                        "AccountService.introduction.admin", "系统管理员。"))
                .owner(admin)
                .build();
        return repository.save(user);
    }
}
