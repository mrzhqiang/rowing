package com.github.mrzhqiang.rowing.modules.account;

import javax.annotation.Nullable;

/**
 * 学生账户服务。
 * <p>
 * 提供对学生账户的各种操作服务。
 */
public interface StudentAccountService {

    /**
     * 通过学生表单注册学生账户。
     *
     * @param form 学生表单。
     * @return 学生账户。可能为 null 值，表示学号已存在。
     */
    @Nullable
    StudentAccount register(StudentInfoForm form);
}
