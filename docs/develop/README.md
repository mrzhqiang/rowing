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

项目结构遵循 Maven 及 GitHub 的相关规范，比如 docs 是文档目录，src 是源码目录，另外 web 是前端源码目录。

后端源码包结构设计：

1. 一等公民：包括配置、领域、模块、第三方、工具以及启动类等顶级包
2. 二等公民：以功能为单位，避免过多内容堆叠在模块目录下
3. 三等公民：如果功能比较多，或者比较抽象，可以再细分子功能

简单示例：

```
├─docs                                  ——系统文档
├─├─deploy                                ——部署文档
├─├─project                               ——项目文档
├─├─user                                  ——用户文档
├─src                                   ——后端源码
├─├─main                                  ——Maven 主目录
├─├─├─java                                  ——java 源代码
├─├─├─├─com.github.mrzhqiang.rowing             ——顶层包
├─├─├─├─├─config                                    ——配置[1]
├─├─├─├─├─domain                                    ——领域[1]
├─├─├─├─├─module                                    ——模块[1]
├─├─├─├─├─├─account                                    ——账户[2]
├─├─├─├─├─├─action                                     ——操作[2]
├─├─├─├─├─├─dict                                       ——字典[2]
├─├─├─├─├─├─exception                                  ——异常[2]
├─├─├─├─├─├─i18n                                       ——国际化[2]
├─├─├─├─├─├─init                                       ——初始化[2]
├─├─├─├─├─├─menu                                       ——菜单[2]
├─├─├─├─├─├─monitor                                    ——监控[2]
├─├─├─├─├─├─session                                    ——会话[2]
├─├─├─├─├─├─setting                                    ——设置[2]
├─├─├─├─├─third                                     ——第三方[1]
├─├─├─├─├─util                                      ——工具[1]
├─├─├─├─├─RowingApplication                         ——启动类[1]
├─├─├─resources                             ——项目资源
├─├─test                                  ——单元测试
├─web                                   ——前端源码
```

前端源码包结构设计：

1. 一等公民：顶级源码包，包含资产、组件、模块等
2. 二等公民：主要是按类型划分的组件，以及按功能划分的模块
3. 三等公民：可能是子功能，包含在父级功能中

简单示例：

```
├─web                                   ——前端源码
├─├─public                                ——公共目录，包含可访问的公共资源
├─├─src                                   ——源代码目录
├─├─├─assets                                ——资产[1]
├─├─├─components                            ——组件[1]
├─├─├─├─dialog.vue                            ——对话框[2]
├─├─├─├─table.vue                             ——数据表格[2]
├─├─├─module                                ——模块[1]
├─├─├─├─account                                ——账户[2]
├─├─├─├─menu                                   ——菜单[2]
├─├─├─├─route                                  ——路由[2]
├─├─├─App.vue                               ——单页面文件
├─├─├─main.js                               ——执行入口
├─├─├─style.css                             ——样式文件
├─├─index.html                            ——入口页面
├─├─package.json                          ——项目配置文件
├─├─vite.config.js                        ——打包配置文件
```
