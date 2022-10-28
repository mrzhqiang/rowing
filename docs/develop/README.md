开发设计
======

开发设计以功能为单位，按重要程度排序，越靠前表示功能越核心。

---

## 目录

- [ ] [1.初始化](1.init.md)
- [ ] [2.国际化](2.i18n.md)
- [ ] [3.字典](3.dict.md)
- [ ] [4.设置](4.setting.md)
- [ ] [5.菜单](5.menu.md)
- [ ] [6.账户](6.account.md)

## 开发环境

- `git version 2.27.0.windows.1`
- `java version "1.8.0_301"`
- `IntelliJ IDEA 2022.1.3 (Ultimate Edition)`
- `redis_version:6.0.16`
- `DBMS: MySQL (ver. 5.7.37-log)`

## 主要框架

本系统基于 `Spring Boot` 框架，使用 `Redis` 和 `MySQL` 中间件，前端则选择 `Vue` 框架，源码位于 `web` 目录下。

| 框架                                                                    | 备注                 |
|-----------------------------------------------------------------------|--------------------|
| [Spring Boot](https://spring.io/projects/spring-boot)                 | 核心框架               |
| [Spring Security](https://spring.io/projects/spring-security)         | 安全认证               |
| [Spring Data Redis](https://spring.io/projects/spring-data-redis)     | Redis 缓存           |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa)         | JPA 即 ORM 框架       |
| [Spring Data REST](https://spring.io/projects/spring-data-rest)       | 基于 JPA 的 REST 框架   |
| [MySQL Connector Java](https://dev.mysql.com/doc/connector-j/8.0/en/) | MySQL 驱动           |
| [Spring Validation](https://beanvalidation.org/)                      | 参数校验               |
| [Lombok](https://projectlombok.org/)                                  | 便捷方法注解             |
| [UserAgentUtils](https://www.bitwalker.eu/software/user-agent-utils)  | 用户代理解析             |
| [Kaptcha](https://github.com/mrzhqiang/kaptcha-spring-boot-starter)   | 验证码                |
| [Helper](https://github.com/mrzhqiang/helper)                         | Java 常用的辅助工具       |
| [Geoip2](https://dev.maxmind.com/geoip?lang=en)                       | IP 转地理位置           |
| [Rxjava](https://github.com/ReactiveX/RxJava)                         | 可观察序列的异步调用框架       |
| [Okhttp](https://github.com/square/okhttp)                            | 最好用的 HTTP Java 客户端 |
| [Retrofit](https://github.com/square/retrofit)                        | 声明式 RESTFUL 框架     |

## 项目结构

项目结构参考 Maven 及 GitHub 的相关规范，比如 docs 即文档目录，src 即源码目录，另外 web 下存放的是前端源码。

源码目录的包结构：

1. 一等公民：以模块为单位，同时也包括配置、领域、工具以及启动类等公共成员
2. 二等公民：以功能为单位，避免过多内容堆叠在模块目录下
3. 三等公民：功能如果比较多或比较抽象，可以再细分子功能

简单示例（中括号内的数字为一、二、三等公民）：

```
├─docs                                  ——系统文档
├─├─deploy                                ——部署文档
├─├─project                               ——项目文档
├─├─user                                  ——用户文档
├─src                                   ——后端源码
├─├─main                                  ——Maven 主目录
├─├─├─java                                  ——java 源代码
├─├─├─├─com.github.mrzhqiang.rowing             ——顶层包
├─├─├─├─├─account                                   ——账户模块[1]
├─├─├─├─├─├─session                                   ——账户会话[2]
├─├─├─├─├─basic                                     ——基础模块[1]
├─├─├─├─├─├─action                                    ——操作日志[2]
├─├─├─├─├─├─data                                      ——数据字典[2]
├─├─├─├─├─├─exception                                 ——异常处理[2]
├─├─├─├─├─├─i18n                                      ——国际化消息[2]
├─├─├─├─├─config                                    ——配置[1]
├─├─├─├─├─domain                                    ——领域[1]
├─├─├─├─├─menu                                      ——菜单模块[1]
├─├─├─├─├─system                                    ——系统模块[1]
├─├─├─├─├─├─init                                      ——系统初始化[2]
├─├─├─├─├─├─setting                                   ——系统设置[2]
├─├─├─├─├─third                                     ——第三方模块[1]
├─├─├─├─├─├─third                                     ——IP地址[2]
├─├─├─├─├─util                                      ——工具[1]
├─├─├─├─├─RowingApplication                         ——启动类[1]
├─├─├─resources                             ——项目资源
├─├─test                                  ——单元测试
├─web                                   ——前端源码
```
