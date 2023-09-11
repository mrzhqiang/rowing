package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.domain.AuditableExcerpt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;

/**
 * 用户表单。
 */
@Projection(name = "user-form", types = {User.class})
public interface UserForm extends AuditableExcerpt {

    String getNickname();

    String getAvatar();

    String getGender();

    LocalDate getBirthday();

    String getEmail();

    String getPhoneNumber();

    String getIntroduction();

    @Value("#{target.owner.id}")
    String getAccountId();

}
