package com.github.mrzhqiang.rowing.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 基础仓库。
 * <p>
 * 主要规范仓库的实体，以便统一主键的类型。
 * <p>
 * 同时，还可以方便在未来对继承的接口进行改进，比如增加 {@link JpaSpecificationExecutor} 接口，或降级为 {@link PagingAndSortingRepository} 分页排序仓库。
 * <p>
 * 最重要的是，通过设计自己的基础仓库，有利于建设代码生成器。
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#core.web.basic.paging-and-sorting">内置支持 web 请求参数</a>
 * 参考：<a href="https://docs.spring.io/spring-data/rest/docs/current/reference/html/#install-chapter">使用 REST 框架不再手写 CURD 接口</a>
 *
 * @param <E> 实体类型。
 */
@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, Long> {

}
