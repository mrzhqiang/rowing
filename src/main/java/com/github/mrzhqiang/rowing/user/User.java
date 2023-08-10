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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 用户。
 * <p>
 * 表示账户对应的用户信息，通常与账户为一对一关系。
 * <p>
 * 一般包含昵称、头像、性别、生日、电子邮箱、电话号码、简介等字段，属于非安全相关的用户信息。
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
     * <p>
     * 用户信息可以自己修改，所以在实体字段上增加验证注解。
     */
    @NotBlank
    @Size(max = 16)
    @Column(nullable = false, length = 16)
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
    @Column(columnDefinition = "text")
    private String avatar;
    /**
     * 性别。
     * <p>
     * 默认为未知性别，说明用户不打算展示性别。
     * <p>
     * 此时将设置为男性默认头像。
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
     * 电子邮箱。
     */
    @Email
    @Size(max = 500)
    @Column(length = 500)
    private String email;
    /**
     * 电话号码。
     */
    @Pattern(regexp = "^\\+[0-9]{2}[0-9]{11}")
    @Size(max = 20)
    @Column(length = 20)
    private String phoneNumber;
    /**
     * 简介。
     * <p>
     * 简介的最大长度为 200 个字符。
     */
    @Size(max = 200)
    @Column(length = 200)
    private String introduction;

    /**
     * 所属账户。
     * <p>
     * 通常先注册账户，再根据注册时的表单参数生成用户，并将用户与账户关联起来。
     * <p>
     * 如果是通过第三方平台登录系统，且为首次登录，也要先注册账户，并通过第三方平台的相关资料生成用户，并关联对应的账户。
     */
    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    private Account owner;

}
