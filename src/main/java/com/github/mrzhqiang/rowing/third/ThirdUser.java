package com.github.mrzhqiang.rowing.third;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.ThirdUserType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 第三方用户。
 * <p>
 * 通常包含第三方用户类型、统一标识符以及所绑定的账户。
 * <p>
 * 其他资料由于存在更新的可能，且无法实时同步到本系统，因此不进行存储——如果有需要的话，再新增字段存储也不麻烦。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ThirdUser extends AuditableEntity {

    private static final long serialVersionUID = 6978802775118282086L;

    /**
     * 第三方用户类型。
     * <p>
     * 属于必填项，与统一标识符结合，作为第三方平台用户是否已在本系统进行绑定的依据。
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_LENGTH)
    private ThirdUserType type;
    /**
     * 统一标识符。
     * <p>
     * 用来识别第三方平台用户的统一标识符，必须保证唯一，表示同一平台的用户不能绑定多个系统账户。
     */
    @Size(max = 500)
    @Column(unique = true, updatable = false, length = 500)
    private String uid;
    /**
     * 绑定账户。
     */
    @ToString.Exclude
    @ManyToOne(optional = false)
    private Account account;

}
