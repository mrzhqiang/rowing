package com.github.mrzhqiang.rowing.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 账号仓库。
 * <p>
 * 包含常用的增删改查方法，也可以根据官方文档进行自定义。
 * <p>
 * 参考：
 * <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories">spring data jpa repositories</a>
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByUid(String uid);
}
