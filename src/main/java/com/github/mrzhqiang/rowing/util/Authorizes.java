package com.github.mrzhqiang.rowing.util;

import com.github.mrzhqiang.rowing.domain.AccountType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 授权工具。
 */
@UtilityClass
public class Authorizes {

    public static final String SPLIT_CHAR = ",";

    /**
     * 层次结构。
     * <p>
     * 表示某一授权可以继承其他授权的所有权限，比方说经理也是员工，同样可以拥有员工的所有权限。
     * <p>
     * 因此，管理员可以包含用户的所有权限。
     *
     * @return 层次结构 Map 映射关系。
     */
    public static Map<String, List<String>> hierarchy() {
        Map<String, List<String>> map = Maps.newHashMap();
        // USER 包含 VISITOR 可以访问的内容权限
        map.put(AccountType.USER.getAuthority(), Lists.newArrayList(AccountType.ANONYMOUS.getAuthority()));
        // ADMIN 拥有所有权限
        map.put(AccountType.ADMIN.getAuthority(), Arrays.stream(AccountType.values())
                .filter(it -> !AccountType.ADMIN.equals(it))
                .map(AccountType::getAuthority)
                .collect(Collectors.toList()));
        return map;
    }

}
