package com.github.mrzhqiang.rowing.module.account;

import com.github.mrzhqiang.rowing.module.domain.BaseRepository;

/**
 * 学生账户仓库。
 * <p>
 * 提供给 rest 框架自动生成 CURD 接口。
 */
public interface StudentAccountRepository extends BaseRepository<StudentAccount> {

    /**
     * 检测指定学号是否存在。
     *
     * @param number 学号。
     * @return 返回 true 表示已存在；否则表示不存在。
     */
    boolean existsByNumber(String number);
}
