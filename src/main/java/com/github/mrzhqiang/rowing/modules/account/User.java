package com.github.mrzhqiang.rowing.modules.account;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import java.time.LocalDate;

/**
 * 用户。
 * <p>
 * 通常表示账号下的用户资料。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class User extends AuditableEntity {

    private String nickname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthday;

    @ToString.Exclude
    @OneToOne(mappedBy = "user", optional = false)
    private Account owner;
}