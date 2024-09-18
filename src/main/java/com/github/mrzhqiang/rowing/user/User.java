package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.helper.time.Ages;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

import static com.github.mrzhqiang.rowing.domain.Domains.EMAIL_REGEXP;

/**
 * 用户。
 * <p>
 * 表示账户对应的用户信息，通常与账户为一对一关系。
 * <p>
 * 一般包含昵称、头像、性别、生日、电子邮箱、电话号码、简介等字段。
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends AuditableEntity {

    private static final long serialVersionUID = -2118789360726189512L;

    /**
     * 昵称。
     * <p>
     * 为避免昵称过长，需要限制长度为 16 个字符。
     * <p>
     * 用户信息可以自己修改，所以在实体字段上增加验证注解。
     */
    @NotBlank
    @Size(max = Domains.MAX_USER_NICKNAME_LENGTH)
    @Column(nullable = false, length = Domains.MAX_USER_NICKNAME_LENGTH)
    private String nickname;
    /**
     * 头像。
     * <p>
     * 头像字段存储的是 URL 地址。
     * <p>
     * 默认为空，则前端根据性别展示不同的默认头像。
     * <p>
     * 对于 text 文本类型的字段，通常占据 L + 2 个字节的空间，其中 L < 2^16 字节。
     */
    @Column(columnDefinition = Domains.TEXT_COLUMN_TYPE)
    private String avatar;
    /**
     * 性别。
     * <p>
     * 默认为未知性别，说明用户不打算展示性别。
     * <p>
     * 此时将设置为男性默认头像。
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = Domains.ENUM_LENGTH)
    private Gender gender = Gender.UNKNOWN;
    /**
     * 生日。
     * <p>
     * 关于年龄：可以通过 {@link Ages} 工具类计算周岁，虚岁则通过年份的差值进行计算。
     */
    private LocalDate birthday;
    /**
     * 电子邮箱。
     */
    @Email(regexp = EMAIL_REGEXP)
    @Size(max = Domains.EMAIL_LENGTH)
    @Column(length = Domains.EMAIL_LENGTH)
    private String email;
    /**
     * 电话号码。
     */
    @Size(max = Domains.PHONE_NUMBER_LENGTH)
    @Column(length = Domains.PHONE_NUMBER_LENGTH)
    private String phoneNumber;
    /**
     * 简介。
     */
    @Size(max = Domains.USER_INTRODUCTION_LENGTH)
    @Column(length = Domains.USER_INTRODUCTION_LENGTH)
    private String introduction;

    /**
     * 所属账户。
     * <p>
     * 通常先注册账户，再根据注册时的表单参数生成用户，并将用户与账户关联起来。
     * <p>
     * 如果是通过第三方平台登录系统，且为首次登录，也要先注册账户，并通过第三方平台的相关资料生成用户，并关联对应的账户。
     */
    @OneToOne(optional = false)
    private Account owner;

}
