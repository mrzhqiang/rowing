package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.helper.time.Ages;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import java.time.LocalDate;

/**
 * 用户。
 * <p>
 * 通常表示账号对应的用户资料。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class User extends AuditableEntity {

    /**
     * 昵称。
     * <p>
     * 为避免昵称过长，需要限制长度为 16 个字符。
     */
    @Column(nullable = false, length = 16)
    private String nickname;
    /**
     * 头像地址。
     * <p>
     * 默认为空，则根据性别展示对应的默认头像。
     */
    @Column(length = 500)
    private String avatar;
    /**
     * 性别。
     */
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.UNKNOWN;
    /**
     * 生日。
     * <p>
     * 关于年龄：可以通过 {@link Ages} 工具类计算周岁，虚岁则通过年份的差值进行计算。
     */
    private LocalDate birthday;
    /**
     * 自我介绍。
     */
    @Column(length = 200)
    private String introduction;

    /**
     * 用户所属账号。
     */
    @ToString.Exclude
    @OneToOne(mappedBy = "user", optional = false, cascade = CascadeType.ALL)
    private Account owner;
}
