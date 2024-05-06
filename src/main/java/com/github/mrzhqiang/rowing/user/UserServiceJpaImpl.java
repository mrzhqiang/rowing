package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.helper.random.RandomNumbers;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.Gender;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
import com.github.mrzhqiang.rowing.setting.Settings;
import com.github.mrzhqiang.rowing.util.Authentications;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceJpaImpl implements UserService {

    private final UserMapper mapper;
    private final UserRepository repository;
    private final SettingService settingService;

    @Timed
    @Counted
    @Override
    public UserInfoData findByUsername(String username) {
        return repository.findByOwner_Username(username)
                .map(mapper::toData)
                .orElse(null);
    }

    @Timed
    @Counted
    @Override
    public void binding(Account account) {
        Optional<User> optional = repository.findByOwner(account);
        if (optional.isPresent()) {
            return;
        }

        String username = account.getUsername();
        if (Authentications.ADMIN_USERNAME.equals(username)) {
            createAdminUser(account);
            return;
        }

        AccountType type = account.getType();
        if (AccountType.ADMIN.equals(type)) {
            String adminNickname = settingService.findByCode(Settings.ADMIN_NICKNAME_PREFIX)
                    .map(Setting::getContent)
                    .orElse(generateNickname(Settings.DEF_ADMIN_NICKNAME_PREFIX));
            createUser(account, adminNickname);
            return;
        }

        if (AccountType.USER.equals(type)) {
            String userNickname = settingService.findByCode(Settings.USER_NICKNAME_PREFIX)
                    .map(Setting::getContent)
                    .orElse(generateNickname(Settings.DEF_USER_NICKNAME_PREFIX));
            createUser(account, userNickname);
            return;
        }

        String anonymousNickname = settingService.findByCode(Settings.ANONYMOUS_NICKNAME_PREFIX)
                .map(Setting::getContent)
                .orElse(generateNickname(Settings.DEF_ANONYMOUS_NICKNAME_PREFIX));
        createUser(account, anonymousNickname);
    }

    private void createAdminUser(Account admin) {
        // 通常只创建一个系统管理员账户，这个方法只会被调用一次，所以这里直接创建
        User user = User.builder()
                .nickname(I18nHolder.getAccessor().getMessage(
                        "AccountService.nickname.admin", "系统管理员"))
                .gender(Gender.MALE)
                // 2019-10-25 -- 2023-12-31 = 1528
                .birthday(LocalDate.of(2019, Month.of(10), 25))
                .email(settingService.findByCode(Settings.SYSTEM_ADMIN_EMAIL)
                        .map(Setting::getContent)
                        .orElse(""))
                .phoneNumber(settingService.findByCode(Settings.SYSTEM_ADMIN_PHONE_NUMBER)
                        .map(Setting::getContent)
                        .orElse(""))
                .introduction(I18nHolder.getAccessor().getMessage(
                        "AccountService.introduction.admin", "系统管理员。"))
                .owner(admin)
                .build();
        repository.save(user);
    }

    private void createUser(Account account, String nickname) {
        User user = account.getUser();
        if (user == null) {
            repository.save(User.builder()
                    .nickname(nickname)
                    .owner(account)
                    .build());
        }
    }

    private String generateNickname(String prefix) {
        // TODO 系统设置可配置生成昵称的规则，比如随机字符串，随机数字后缀，随机百家姓+名字等等规则
        int postfix = RandomNumbers.rangeInt(Domains.MIN_NICKNAME_GENERATE_LENGTH,
                Domains.MAX_USER_NICKNAME_LENGTH - prefix.length());
        return prefix + postfix;
    }

}
