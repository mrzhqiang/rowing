package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.user.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 账户映射器。
 * <p>
 * 关于映射器：
 * <p>
 * 1. 隐式转换，Mapper 自带功能，对于基本数据类型及其包装类，可以通过 parse 和 toString 完成转换。
 * <p>
 * 2. 显式转换，有多种方式实现：
 * <p>
 * 2.1 定义接口的默认方法，转换的类型要一一对应，一般需要添加 Mapping 注解及 target 和 source 参数。
 * <p>
 * 2.2 定义抽象类的公开方法，类型同上，可以在抽象类中引入 Spring 框架的 ConversionService 实例，实现类型的自动转换。
 * <p>
 * 2.3 定义转换类，通过 Named 或 Qualifier 注解定义方法，以实现精准匹配，需要在 Mapper 注解中 uses 转换类，且在 Mapping 注解中声明对应的命名。
 * <p>
 * 2.4 定义 Java 代码，通过调用公开的静态方法，或 Mapper 接口的默认方法，实现相关转换。
 * <p>
 * 通常，两种类型的简单映射，我们采用 2.1 方式转换。如果有现成的工具类可以使用，那么使用 2.4 会比较方便。
 * <p>
 * 对于复杂类型的转换，通常是定义抽象类以及转换 Context 进行处理。
 * <p>
 * 鉴于映射器应该纯粹和简单，我们推荐只使用 2.1 和 2.4 方式进行转换。
 */
@Mapper(componentModel = "spring", imports = {AccountType.class})
public interface AccountMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "form.username")
    @Mapping(target = "password", source = "form.password")
    Account toEntity(PasswordConfirmForm form);

    @Mapping(target = "owner", source = "account")
    User toUser(RegisterForm form, Account account);

}
