package com.github.mrzhqiang.rowing.convert;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mrzhqiang.helper.Splitters;
import com.github.mrzhqiang.rowing.util.Jsons;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.domain.Example;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * 字符串转查询示例的转换器。
 * <p>
 * 用于将查询参数转换为 {@link org.springframework.data.domain.Example} 示例。
 * <p>
 * 注意：Spring Data REST 本身不支持 HTTP 查询参数转为 JpaRepository 仓库的 Example 查询参数。
 * <p>
 * 不支持的原因有很多，其中一个比较重要的原因是：接收 Example 参数需要自定义 Repository 方法，
 * 自定义 Repository 方法被映射为类似 /api/[repository-path]/search/[method-path] 的搜索路径，
 * 而 REST 的搜索路径仅支持 GET 方式访问，这将导致请求参数长度受到限制(2kb ~ 8kb)，
 * 且一些特殊字符会被 URL 自动编码，要将复杂的查询条件和查询内容解析出来，需要做很多额外的事情，
 * 完全比不上自定义一个 Controller 接口来得简单。
 * <p>
 * 后记：
 * <p>
 * 1. 使用 XXXContaining 查询字符串相关的字段，只要保证传入参数为 '' 空串，即表示查询所有内容，不过滤此字段相关的数据。
 * <p>
 * 2. 使用 In 查询枚举相关的字段，只要保证传入参数为所有枚举，即表示查询所有内容，不过滤此字段的相关数据。
 * <p>
 * 3. 使用 @Query 自定义查询，非原生的自定义查询支持面向对象进行伪 sql 方式查询，可以建立更灵活的多字段搜索查询。
 *
 * @deprecated 这个转换器仅作为一个错误方向的尝试，不要使用。
 */
@Deprecated
public final class StringToExampleConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Example.class));
    }

    @Override
    public Object convert(Object source, @Nonnull TypeDescriptor sourceType, @Nonnull TypeDescriptor targetType) {
        if (ObjectUtils.isEmpty(source)) {
            return null;
        }
        Class<?> entityClass = targetType.getResolvableType().getGeneric(0).getRawClass();
        return convertExample(source.toString(), entityClass);
    }

    private <T> Example<T> convertExample(String params, Class<T> entityClass) {
        // 请求参数仅支持简单类型字段，不支持复杂对象字段
        ObjectNode rootNode = Jsons.newObject();
        // params 使用 , 逗号分割字段名称和字段值，字段名称与字段值通过 - 破折号分割
        // 因此查询参数受到限制，不能包含 , 逗号和 - 破折号，否则将干扰解析
        Splitters.COMMA.split(params).forEach(it -> {
            Iterator<String> iterator = Splitters.DASH.split(it).iterator();
            String fieldName = "";
            String fieldValue = "";
            if (iterator.hasNext()) {
                fieldName = iterator.next();
                if (iterator.hasNext()) {
                    fieldValue = iterator.next();
                }
            }
            if (StringUtils.hasText(fieldName) && StringUtils.hasText(fieldValue)) {
                rootNode.put(fieldName, fieldValue);
            }
        });
        // 这里仅支持完全匹配的查询，如果要支持多种样式的查询，可以在自定义 Repository 方法的 Example 参数上，
        // 添加自定义 @MatcherType 注解，注解的值是 ExampleMatcher 数组，此时可以定义多种匹配方式，比如模糊匹配。
        // 提示：通过 targetType.getAnnotation(DurationFormat.class); 代码获取 Example 参数前面的注解
        return Example.of(Jsons.fromJson(rootNode, entityClass));
    }

}
