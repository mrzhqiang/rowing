package com.github.mrzhqiang.rowing.core.data;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 数据字典扫描器。
 * <p>
 * 扫描指定基础包下的所有枚举类，提供给调用方使用。
 *
 * @see ClassPathScanningCandidateComponentProvider
 */
@Slf4j
@Component
public class DataDictScanner {

    private static final String RESOURCE_TEMPLATE = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "%s/**/*.class";

    private final ResourcePatternResolver resourcePatternResolver;
    private final MetadataReaderFactory metadataReaderFactory;

    public DataDictScanner(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    /**
     * 扫描基于枚举的内置数据字典。
     *
     * @return 包含枚举 class 对象的列表。
     */
    @SuppressWarnings("unchecked")
    public List<Class<? extends Enum<?>>> scanEnum(String basePackage) {
        Preconditions.checkNotNull(basePackage, "base package == null");

        List<Class<? extends Enum<?>>> enums = Lists.newArrayList();
        try {
            basePackage = ClassUtils.convertClassNameToResourcePath(basePackage);
            String packageSearchPath = Strings.lenientFormat(RESOURCE_TEMPLATE, basePackage);
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

            boolean traceEnabled = log.isTraceEnabled();
            boolean debugEnabled = log.isDebugEnabled();

            for (Resource resource : resources) {
                if (traceEnabled) {
                    log.trace("开始扫描：" + resource);
                }
                try {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    ClassMetadata classMetadata = metadataReader.getClassMetadata();
                    String className = classMetadata.getClassName();
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isEnum()) {
                        if (debugEnabled) {
                            log.debug("扫描到数据字典枚举：{}", clazz.getName());
                        }
                        enums.add((Class<? extends Enum<?>>) clazz);
                    }
                } catch (FileNotFoundException ex) {
                    if (traceEnabled) {
                        log.trace("忽略不可读 " + resource + ": " + ex.getMessage());
                    }
                } catch (Throwable ex) {
                    throw new RuntimeException("读取候选数据字典枚举类失败： " + resource, ex);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("类路径扫描期间的 IO 故障", ex);
        }
        return enums;
    }
}
