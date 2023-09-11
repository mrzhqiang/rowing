package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.domain.AuditableExcerpt;
import com.github.mrzhqiang.rowing.domain.Gender;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;

/**
 * 用户摘要。
 */
@Projection(name = "user-excerpt", types = {User.class})
public interface UserExcerpt extends AuditableExcerpt {

    String getNickname();

    String getAvatar();

    Gender getGender();

    LocalDate getBirthday();

    String getEmail();

    String getPhoneNumber();

    String getIntroduction();

}
