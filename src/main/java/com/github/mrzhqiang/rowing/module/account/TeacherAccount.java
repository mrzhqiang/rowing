package com.github.mrzhqiang.rowing.module.account;

import com.github.mrzhqiang.rowing.module.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * 教师账户。
 * <p>
 * <p>
 * 与系统账户一对一绑定的教师账户。
 * <p>
 * TODO 暂时只有姓名，可以和对接的系统资料进行匹配，丰富这里的字段。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class TeacherAccount extends AuditableEntity {

    /**
     * 教师编号。
     */
    @Column(nullable = false, unique = true)
    private String number;
    /**
     * 全名。
     * <p>
     * 即身份证姓名。
     */
    private String fullName;
    /**
     * 账号。
     * <p>
     * 一对一绑定，则通过教师编号可以找到对应的系统账户。
     */
    @OneToOne
    private Account account;
}
