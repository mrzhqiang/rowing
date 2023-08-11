package com.github.mrzhqiang.rowing.third;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.ThirdUserType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = MAX_ENUM_NAME_LENGTH)
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
    @ManyToOne(optional = false)
    private Account account;

}
