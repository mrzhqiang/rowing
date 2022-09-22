package com.github.mrzhqiang.rowing.core.account;

import com.github.mrzhqiang.rowing.core.domain.BaseRepository;

/**
 * 教师账户仓库。
 * <p>
 * 提供给 rest 框架自动生成 CURD 接口。
 */
public interface TeacherAccountRepository extends BaseRepository<TeacherAccount> {

    boolean existsByNumber(String number);
}
