package com.github.mrzhqiang.rowing.core.account;

import com.github.mrzhqiang.rowing.core.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.core.data.DataDictItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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

    /**
     * 昵称。
     */
    private String nickname;
    /**
     * 性别。
     */
    @ManyToOne
    private DataDictItem gender;
    /**
     * 生日。
     * <p>
     * 可以用来计算年龄。
     */
    private LocalDate birthday;

    @ToString.Exclude
    @OneToOne(mappedBy = "user", optional = false)
    private Account owner;
}
