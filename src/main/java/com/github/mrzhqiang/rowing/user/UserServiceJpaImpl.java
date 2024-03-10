package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.helper.random.RandomNumbers;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.account.Accounts;
import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.Gender;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
import com.github.mrzhqiang.rowing.setting.Settings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceJpaImpl implements UserService {

    private final UserMapper mapper;
    private final UserRepository repository;
    private final SettingService settingService;

    public UserServiceJpaImpl(UserMapper mapper,
                              UserRepository repository,
                              SettingService settingService) {
        this.mapper = mapper;
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
    public void binding(Account account) {
        Optional<User> optional = repository.findByOwner(account);
        if (optional.isPresent()) {
            return;
        }

        String username = account.getUsername();
        if (Accounts.ADMIN_USERNAME.equals(username)) {
            createSuperAdmin(account);
            return;
        }

        AccountType type = account.getType();
        if (AccountType.ADMIN.equals(type)) {
            createAdmin(account);
            return;
        }

        if (AccountType.USER.equals(type)) {
            createUser(account);
            return;
        }

        createAnonymous(account);
    }

    private void createSuperAdmin(Account admin) {
        // 通常只创建一个 admin 账户，所以这里直接创建
        User user = User.builder()
                .nickname(I18nHolder.getAccessor().getMessage(
                        "AccountService.nickname.admin", "超级管理员"))
                .gender(Gender.MALE)
                // 20191025 - 20231231 = 1528
                .birthday(LocalDate.of(2019, Month.of(10), 25))
                .email(settingService.findByCode(Settings.ADMIN_EMAIL)
                        .map(Setting::getContent)
                        .orElse(""))
                .phoneNumber(settingService.findByCode(Settings.ADMIN_PHONE_NUMBER)
                        .map(Setting::getContent)
                        .orElse(""))
                .introduction(I18nHolder.getAccessor().getMessage(
                        "AccountService.introduction.admin", "超级管理员。"))
                .owner(admin)
                .build();
        repository.save(user);
    }

    private void createAdmin(Account admin) {
        User user = admin.getUser();
        if (user == null) {
            String nickname = settingService.findByCode(Settings.ADMIN_NICKNAME_PREFIX)
                    .map(Setting::getContent)
                    .map(this::generateNickname)
                    .orElse(generateNickname(Settings.DEF_ADMIN_NICKNAME_PREFIX));
            user = User.builder()
                    .nickname(nickname)
                    .owner(admin)
                    .build();
            repository.save(user);
        }
    }

    private void createUser(Account account) {
        User user = account.getUser();
        if (user == null) {
            String nickname = settingService.findByCode(Settings.USER_NICKNAME_PREFIX)
                    .map(Setting::getContent)
                    .orElse(generateNickname(Settings.DEF_USER_NICKNAME_PREFIX));
            user = User.builder()
                    .nickname(nickname)
                    .owner(account)
                    .build();
            repository.save(user);
        }
    }

    private void createAnonymous(Account anonymous) {
        User user = anonymous.getUser();
        if (user == null) {
            String nickname = settingService.findByCode(Settings.ANONYMOUS_NICKNAME_PREFIX)
                    .map(Setting::getContent)
                    .orElse(generateNickname(Settings.DEF_ANONYMOUS_NICKNAME_PREFIX));
            user = User.builder()
                    .nickname(nickname)
                    .owner(anonymous)
                    .build();
            repository.save(user);
        }
    }

    private String generateNickname(String prefix) {
        return prefix + RandomNumbers.rangeInt(Domains.MIN_NICKNAME_GENERATE_LENGTH,
                Domains.MAX_USER_NICKNAME_LENGTH - prefix.length());
    }

}
