package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.helper.random.RandomNumbers;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.Gender;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
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

        AccountType type = account.getType();
        if (AccountType.ADMIN.equals(type)) {
            createAdmin(account);
            return;
        }

        createUser(account);
    }

    private void createAdmin(Account admin) {
        User user = User.builder()
                .nickname(I18nHolder.getAccessor().getMessage(
                        "AccountService.nickname.admin", "管理员"))
                .gender(Gender.MALE)
                .birthday(LocalDate.of(2019, Month.of(10), 25))
                .email(settingService.findByCode(SettingService.ADMIN_EMAIL)
                        .map(Setting::getContent)
                        .orElse(""))
                .phoneNumber(settingService.findByCode(SettingService.ADMIN_PHONE_NUMBER)
                        .map(Setting::getContent)
                        .orElse(""))
                .introduction(I18nHolder.getAccessor().getMessage(
                        "AccountService.introduction.admin", "系统管理员。"))
                .owner(admin)
                .build();
        repository.save(user);
    }

    private void createUser(Account account) {
        User user = account.getUser();
        if (user == null) {
            String nickname = settingService.findByCode("userNicknamePrefix")
                    .map(Setting::getContent)
                    .orElse("用户" + RandomNumbers.rangeInt(6, Domains.USER_NICKNAME_LENGTH - 2));
            user = User.builder()
                    .nickname(nickname)
                    .owner(account)
                    .build();
            repository.save(user);
        }
    }

}
