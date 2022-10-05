开发文档
======

主要讨论开发中的一些基本思路。

---

## 文档目录

- [x] [1.数据字典](1.data-dict.md)
- [x] [2.系统设置](2.sys-setting.md)
- [x] [3.系统初始化](3.sys-init.md)
- [x] [4.系统账户](4.sys-account.md)
- [ ] [5.系统菜单](5.sys-menu.md)

## 开发环境

- `git version 2.27.0.windows.1`
- `java version "1.8.0_301"`
- `IntelliJ IDEA 2022.1.3 (Ultimate Edition)`
- `redis_version:6.0.16`
- `DBMS: MySQL (ver. 5.7.37-log)`

## 主要框架

本系统基于 `Spring Boot` 框架，使用 `Redis` 和 `MySQL` 中间件，前端则选择 `Vue3` 框架，源码位于 `web` 目录下。

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

基础的项目结构参考 Maven 及 GitHub 的规范，比如 docs 目录即文档目录，src 目录即源码目录，另外 web 目录下存放前端源码。

对于 src 目录下的包结构，选择整体抽象为一等公民，而具体业务为二等公民，同时如果业务过多，可以归纳到一个集合，若集合为二等公民，那么所包含的业务为三等公民。

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
├─├─├─├─├─config                                    ——配置包[1]
├─├─├─├─├─domain                                    ——领域包[1]
├─├─├─├─├─module                                    ——模块包[1]
├─├─├─├─├─├─account                                     ——账户模块包[2]
├─├─├─├─├─├─menu                                        ——菜单模块包[2]
├─├─├─├─├─├─system                                      ——系统包[2]
├─├─├─├─├─├─├─data                                          ——系统数据包[3]
├─├─├─├─├─├─├─init                                          ——系统初始化包[3]
├─├─├─├─├─├─├─session                                       ——系统会话包[3]
├─├─├─├─├─├─├─setting                                       ——系统设置包[3]
├─├─├─├─├─util                                      ——工具包[1]
├─├─├─├─├─RowingApplication                         ——启动类
├─├─├─resources                             ——项目资源
├─├─test                                  ——单元测试
├─web                                   ——前端源码
...
```