package com.github.mrzhqiang.rowing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import static com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter.serializeAllExcept;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具。
 * <p>
 * 以 Spring Boot 默认采用的 Jackson 框架为主。
 */
public final class Jsons {
    private Jsons() {
        // no instances
    }

    /*
     * 排除敏感属性字段
     */
    private static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setFilterProvider(
            new SimpleFilterProvider().addFilter("fieldFilter", serializeAllExcept(EXCLUDE_PROPERTIES)));

    /**
     * 创建一个全新的对象节点，可以自由组装。
     */
    public static ObjectNode newObject() {
        return OBJECT_MAPPER.createObjectNode();
    }

    /**
     * 创建一个全新的数组节点，可以自由组装。
     */
    public static ArrayNode newArray() {
        return OBJECT_MAPPER.createArrayNode();
    }

    /**
     * 解析字符串为 JsonNode 对象，以便修改其中的内容。
     */
    @SneakyThrows
    public static JsonNode parse(String content) {
        Preconditions.checkNotNull(content, "content == null");
        Preconditions.checkArgument(StringUtils.hasText(content), "content has not text");
        return OBJECT_MAPPER.readTree(content);
    }

    /**
     * 从 JsonNode 对象转换为指定类型的实例。
     */
    @SneakyThrows
    public static <T> T fromJson(JsonNode node, Class<T> clazz) {
        Preconditions.checkNotNull(node, "node == null");
        Preconditions.checkNotNull(clazz, "clazz == null");
        return OBJECT_MAPPER.treeToValue(node, clazz);
    }

    /**
     * 从 json 字符串转换为指定类型的实例。
     */
    @SneakyThrows
    public static <T> T from(String content, Class<T> clazz) {
        Preconditions.checkNotNull(content, "content == null");
        Preconditions.checkArgument(StringUtils.hasText(content), "content has not text");
        Preconditions.checkNotNull(clazz, "clazz == null");
        return OBJECT_MAPPER.readValue(content, clazz);
    }

    /**
     * 从 json 文件转换为指定类型的实例。
     */
    @SneakyThrows
    public static <T> T fromFile(File jsonFile, Class<T> clazz) {
        Preconditions.checkNotNull(jsonFile, "json file == null");
        Preconditions.checkArgument(jsonFile.exists(), "json file is not exists");
        Preconditions.checkArgument(jsonFile.isFile(), "json file is not file");
        Preconditions.checkNotNull(clazz, "clazz == null");
        return OBJECT_MAPPER.readValue(jsonFile, clazz);
    }

    /**
     * 从 json 字符串转换为包含指定类型元素的 List 实例。
     */
    @SneakyThrows
    public static <T> List<T> listFrom(String content, Class<T> clazz) {
        Preconditions.checkNotNull(content, "content == null");
        Preconditions.checkArgument(StringUtils.hasText(content), "content has not text");
        Preconditions.checkNotNull(clazz, "clazz == null");
        ArrayType type = OBJECT_MAPPER.getTypeFactory().constructArrayType(clazz);
        return OBJECT_MAPPER.readValue(content, type);
    }

    /**
     * 从 json 文件转换为包含指定类型元素的 List 实例。
     */
    @SneakyThrows
    public static <T> List<T> listFromFile(File jsonFile, Class<T> clazz) {
        Preconditions.checkNotNull(jsonFile, "json file == null");
        Preconditions.checkArgument(jsonFile.exists(), "json file is not exists");
        Preconditions.checkArgument(jsonFile.isFile(), "json file is not file");
        Preconditions.checkNotNull(clazz, "clazz == null");
        CollectionType type = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        return OBJECT_MAPPER.readValue(jsonFile, type);
    }

    /**
     * 从 json 字符串转换为指定 key 和 value 类型的 Map 实例。
     */
    @SneakyThrows
    public static <K, V> Map<K, V> mapFrom(String content, Class<K> keyClass, Class<V> valueClass) {
        Preconditions.checkNotNull(content, "content == null");
        Preconditions.checkArgument(StringUtils.hasText(content), "content has not text");
        Preconditions.checkNotNull(keyClass, "key clazz == null");
        Preconditions.checkNotNull(valueClass, "value clazz == null");
        MapType type = OBJECT_MAPPER.getTypeFactory().constructMapType(HashMap.class, keyClass, valueClass);
        return OBJECT_MAPPER.readValue(content, type);
    }

    /**
     * 从 json 文件转换为指定 key 和 value 类型的 Map 实例。
     */
    @SneakyThrows
    public static <K, V> Map<K, V> mapFromFile(File jsonFile, Class<K> keyClass, Class<V> valueClass) {
        Preconditions.checkNotNull(jsonFile, "json file == null");
        Preconditions.checkArgument(jsonFile.exists(), "json file is not exists");
        Preconditions.checkArgument(jsonFile.isFile(), "json file is not file");
        Preconditions.checkNotNull(keyClass, "key clazz == null");
        Preconditions.checkNotNull(valueClass, "value clazz == null");
        MapType type = OBJECT_MAPPER.getTypeFactory().constructMapType(HashMap.class, keyClass, valueClass);
        return OBJECT_MAPPER.readValue(jsonFile, type);
    }

    /**
     * 将任意对象转换为 JsonNode 对象。
     */
    public static JsonNode toJson(Object data) {
        return OBJECT_MAPPER.valueToTree(data);
    }

    /**
     * 将 JsonNode 对象转换为 json 字符串。
     */
    @SneakyThrows
    public static String stringify(JsonNode json) {
        return generateJson(json, false, false);
    }

    /**
     * 将 JsonNode 对象转换为 ascii json 字符串。
     */
    @SneakyThrows
    public static String asciiStringify(JsonNode json) {
        return generateJson(json, false, true);
    }

    /**
     * 将 JsonNode 对象转换为漂亮的 json 字符串格式。
     */
    @SneakyThrows
    public static String prettyPrint(JsonNode json) {
        return generateJson(json, true, false);
    }

    private static String generateJson(Object o, boolean prettyPrint, boolean escapeNonAscii) throws JsonProcessingException {
        ObjectWriter writer = OBJECT_MAPPER.writer();
        if (prettyPrint) {
            writer.with(SerializationFeature.INDENT_OUTPUT);
        }
        if (escapeNonAscii) {
            writer.with(JsonWriteFeature.ESCAPE_NON_ASCII);
        }
        return writer.writeValueAsString(o);
    }

    /**
     * 将任意对象转换为 json 字符串。
     */
    @SneakyThrows
    public static String toString(Object data) {
        return OBJECT_MAPPER.writeValueAsString(data);
    }

    /**
     * 将任意对象转换为 Map 实例。
     */
    public static Map<String, Object> toMap(Object data) {
        if (data == null) {
            return Collections.emptyMap();
        }

        String content = Jsons.toString(data);
        return Jsons.mapFrom(content, String.class, Object.class);
    }
}
