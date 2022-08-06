开发设计
======

文本档主要讨论开发中的一些设计思路。

---

## 环境准备

- `git version 2.27.0.windows.1`
- `java version "1.8.0_301"`
- `IntelliJ IDEA 2022.1.3 (Ultimate Edition)`
- `redis_version:6.0.16`
- `DBMS: MySQL (ver. 5.7.37-log)`

## 设计文档

- [x] [1.数据字典](1.data-dict.md)
- [x] [2.系统设置](2.sys-setting.md)
- [x] [3.系统初始化](3.sys-init.md)
- [x] [4.账户](4.account.md)
- [ ] [5.菜单](5.menu.md)

## 主要框架

本系统基于 `Spring Boot` 框架，使用 `Redis` 和 `MySQL` 中间件，前端则选择 `Vue3` 框架，源码位于 `web` 目录下。

| 框架                                                                           | 备注                 |
|------------------------------------------------------------------------------|--------------------|
| [Spring Boot](https://spring.io/projects/spring-boot)                        | 核心框架               |
| [Spring Security](https://spring.io/projects/spring-security)                | 安全认证               |
| [Spring Data Redis](https://spring.io/projects/spring-data-redis)            | Redis 缓存           |
| [Spring Session Redis](https://spring.io/projects/spring-session-data-redis) | Redis 会话           |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa)                | 关系型数据库 ORM         |
| [MySQL Connector Java](https://dev.mysql.com/doc/connector-j/8.0/en/)        | MySQL 驱动           |
| [Spring Validation](https://beanvalidation.org/)                             | 参数校验               |
| [Lombok](https://projectlombok.org/)                                         | 便捷方法               |
| [UserAgentUtils](https://www.bitwalker.eu/software/user-agent-utils)         | 用户代理解析             |
| [Kaptcha](https://github.com/mrzhqiang/kaptcha-spring-boot-starter)          | 验证码                |
| [Helper](https://github.com/mrzhqiang/helper)                                | Java 常用的辅助工具       |
| [Geoip2](https://dev.maxmind.com/geoip?lang=en)                              | IP 转地理位置           |
| [Rxjava](https://github.com/ReactiveX/RxJava)                                | 可观察序列的异步调用框架       |
| [Okhttp](https://github.com/square/okhttp)                                   | 最好用的 HTTP Java 客户端 |
| [Retrofit](https://github.com/square/retrofit)                               | 声明式 RESTFUL 框架     |

## 开发规范

核心是遵循 Java 面向对象开发的基本原则。

### 命名规范

基本命名一律使用小写字母，包括项目名称和包名称，非必要情况下，只能使用一个单词，允许使用缩写精简命名，但必须有相应的说明文字。

类名称一律使用驼峰命名，且遵循动词在前名词在后的原则，另可参考 RESTFUL 在命名上的相关规范。

文件名称一律使用小写字母，允许使用 - _ . 三种特殊符号进行连接。

其他命名根据使用场景灵活应用，但总的来说，不应脱离以上三种命名方式的范围。

### 结构规范

公共的抽象概念为一等公民，具体的业务集合为二等公民，同时如果业务集合过多，可以归纳到一个子集，则此子集为二等公民，所包含的业务集合为三等公民。

简单示例：

```
├─docs                                  ——系统文档
├─├─deploy                                ——部署文档
├─├─project                               ——项目文档
├─├─user                                  ——用户文档
├─src                                   ——源代码
├─├─main                                  ——Maven 主目录
├─├─├─java                                  ——java 源代码
├─├─├─├─com.github.mrzhqiang.rowing             ——代码根目录
├─├─├─├─├─config                                    ——配置包[1]
├─├─├─├─├─core                                      ——核心包[1]
├─├─├─├─├─├─account                                      ——账户包[2]
├─├─├─├─├─├─session                                      ——会话包[2]
├─├─├─├─├─├─system                                       ——系统包[2]
├─├─├─├─├─├─├─init                                          ——初始化包[3]
├─├─├─├─├─util                                      ——工具包[1]
├─├─├─├─├─RowingApplication                         ——启动类[1]
├─├─├─resources                             ——项目资源
├─├─test                                  ——单元测试
├─web                                   ——前端 vue3 源码
...
```