package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.system.data.DataDictItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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

    @OneToOne(mappedBy = "user")
    private Account account;

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

    /**
     * 根据当前日期计算年龄。
     *
     * @return 年龄，从生日开始，距离今天有多少年。
     */
    public Integer getAge() {
        return Optional.ofNullable(birthday)
                .map(it -> ChronoUnit.YEARS.between(it, LocalDate.now()))
                .map(Long::intValue)
                .orElse(0);
    }
}
