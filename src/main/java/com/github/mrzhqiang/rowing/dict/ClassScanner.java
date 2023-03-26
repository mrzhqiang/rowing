package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.helper.Exceptions;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.support.MessageSourceAccessor;
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

import java.util.List;

/**
 * 类扫描器。
 *
 * @see ClassPathScanningCandidateComponentProvider
 */
@Slf4j
@Component
public final class ClassScanner {

    private static final String RESOURCE_TEMPLATE = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "%s/**/*.class";

    private final ResourcePatternResolver resourcePatternResolver;
    private final MetadataReaderFactory metadataReaderFactory;
    private final MessageSourceAccessor sourceAccessor;

    public ClassScanner(ResourceLoader resourceLoader, MessageSource messageSource) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
        this.sourceAccessor = new MessageSourceAccessor(messageSource);
    }

    /**
     * 基于包路径扫描枚举。
     *
     * @return 包含枚举 Class 对象的列表。
     */
    @SuppressWarnings("unchecked")
    public List<Class<? extends Enum<?>>> scanEnumBy(String basePackage) {
        Preconditions.checkNotNull(basePackage, "base package == null");

        String startMessage = sourceAccessor.getMessage("ClassScanner.scanEnum.start",
                new Object[]{basePackage},
                Strings.lenientFormat("准备扫描 %s 包下的所有枚举", basePackage));
        log.info(startMessage);

        Stopwatch stopwatch = Stopwatch.createStarted();
        List<Class<? extends Enum<?>>> enums = Lists.newArrayList();
        try {
            basePackage = ClassUtils.convertClassNameToResourcePath(basePackage);
            String packageSearchPath = Strings.lenientFormat(RESOURCE_TEMPLATE, basePackage);
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

            for (Resource resource : resources) {
                try {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    ClassMetadata classMetadata = metadataReader.getClassMetadata();
                    String className = classMetadata.getClassName();
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isEnum()) {
                        if (log.isDebugEnabled()) {
                            String nameMessage = sourceAccessor.getMessage("ClassScanner.scanEnum.success",
                                    new Object[]{resource, className},
                                    Strings.lenientFormat("扫描成功，资源文件 %s 枚举类：%s", resource, className));
                            log.debug(nameMessage);
                        }
                        enums.add((Class<? extends Enum<?>>) clazz);
                    }
                } catch (Exception e) {
                    String message = Exceptions.ofMessage(e);
                    String notFoundMessage = sourceAccessor.getMessage("ClassScanner.scanEnum.failure",
                            new Object[]{resource, message},
                            Strings.lenientFormat("扫描失败，资源文件 %s 失败原因：%s", resource, message));
                    log.warn(notFoundMessage);
                }
            }
        } catch (Exception ex) {
            String message = Exceptions.ofMessage(ex);
            String exceptionMessage = sourceAccessor.getMessage("ClassScanner.scanEnum.error",
                    new Object[]{message},
                    Strings.lenientFormat("扫描出错，原因：%s", message));
            throw new RuntimeException(exceptionMessage, ex);
        }

        int total = enums.size();
        Stopwatch stop = stopwatch.stop();
        String finishedMessage = sourceAccessor.getMessage("ClassScanner.scanEnum.finished",
                new Object[]{total, stop},
                Strings.lenientFormat("扫描完成，一共找到 %s 个枚举类，用时：%s", total, stop));
        log.info(finishedMessage);
        return enums;
    }
}
