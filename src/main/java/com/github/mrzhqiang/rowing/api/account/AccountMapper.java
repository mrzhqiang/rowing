package com.github.mrzhqiang.rowing.api.account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * 账号映射器。
 * <p>
 * 一般来说，表单 --> 实体 --> 数据，它们的内容基本一致，但有的字段不能作为直接传输，比如密码、创建人等等，
 * 有的字段必须格式化之后才能传输，比如上次访问时间等等，那么利用 Mapper 来做转换非常方便，不用在实体中定义
 * 各种 Json 序列化及反序列化策略，节省了大量时间和精力，并且避免了各种出错的可能性。
 * <p>
 * 转换规则如下：
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
 * 鉴于映射器应该纯粹和简单，我们推荐只使用 2.1 和 2.4 方式进行转换，复杂的转换应该先进行数据化处理，再声明数据类型与转换类型的映射关系即可。
 * <p>
 * 所谓数据化处理，一个鲜明的例子就是，先通过 Service 拿到对应的 Data 类，之后的映射就变得简单了。
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountData toData(Account entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "firstFailed", ignore = true)
    @Mapping(target = "failedCount", ignore = true)
    @Mapping(target = "disabled", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Account toEntity(AccountForm form);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(target = "firstFailed", ignore = true)
    @Mapping(target = "failedCount", ignore = true)
    @Mapping(target = "disabled", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void update(AccountForm form, @MappingTarget Account entity);
}
