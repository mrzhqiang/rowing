开发设计
======

开发设计包括：主要功能、开发环境、框架依赖、代码结构这四个部分的内容。

---

## 主要功能

本项目的主要功能有：

- [x] [1.国际化](1.i18n.md)
- [ ] [2.初始化](2.init.md)
- [ ] [3.字典](3.dict.md)
- [ ] [4.设置](4.setting.md)
- [ ] [5.菜单](5.menu.md)
- [ ] [6.账户](6.account.md)

## 开发环境

本项目需要的最小开发环境：

- `git version 2.27.0.windows.1`
- `java version "1.8.0_301"`
- `IntelliJ IDEA 2022.2.3 (Ultimate Edition)`
- `redis_version:6.0.16`
- `DBMS: MySQL (ver. 5.7.37-log)`
- `node.js v16.13.2`
- `npm 8.16.0`
- `@vue/cli 5.0.8`
- `WebStorm 2022.2.3`

## 框架依赖

后端基于 `Spring Boot` 框架，使用 `Redis` 和 `MySQL` 中间件，前端基于 `Vue` 框架，参考 `vue-element-admin` 模板。

此处仅列出最核心的后端依赖，如需了解全部内容，请访问项目中的 `pom.xml` 文件。

| 框架                                                                    | 备注                 |
|-----------------------------------------------------------------------|--------------------|
| [Spring Boot](https://spring.io/projects/spring-boot)                 | 基础框架               |
| [Spring Security](https://spring.io/projects/spring-security)         | 安全框架               |
| [Spring Data Redis](https://spring.io/projects/spring-data-redis)     | Redis 数据框架         |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa)         | JPA 框架             |
| [Spring Data REST](https://spring.io/projects/spring-data-rest)       | 基于 JPA 的 REST 框架   |
| [MySQL Connector Java](https://dev.mysql.com/doc/connector-j/8.0/en/) | MySQL 驱动           |
| [Spring Validation](https://beanvalidation.org/)                      | 参数校验               |
| [Lombok](https://projectlombok.org/)                                  | 便捷方法注解             |
| [UserAgentUtils](https://www.bitwalker.eu/software/user-agent-utils)  | 用户代理解析             |
| [Kaptcha](https://github.com/mrzhqiang/kaptcha-spring-boot-starter)   | 验证码                |
| [Helper](https://github.com/mrzhqiang/helper)                         | Java 常用的辅助工具       |
| [Geoip2](https://dev.maxmind.com/geoip?lang=en)                       | IP 转地理位置           |
| [RxJava](https://github.com/ReactiveX/RxJava)                         | 可观察序列的异步调用框架       |
| [OkHttp](https://github.com/square/okhttp)                            | 最好用的 HTTP Java 客户端 |
| [Retrofit](https://github.com/square/retrofit)                        | 声明式 RESTFUL 框架     |

（前端框架待补充）

## 代码结构

代码需要遵循一定的结构，好的代码结构阅读起来，一定是赏心悦目的。

在本项目中，后端遵循 `Spring Boot` 官方推荐的[代码结构规范][1]，即以功能为主，功能包内有控制器、服务、服务实现、仓库等内容。

```
├─docs                                  ——项目文档
├─├─deployment                             ——安装部署
├─├─develop                                ——开发设计
├─├─guide                                  ——使用指南
├─http                                  ——接口测试目录
├─src                                   ——源代码目录
├─├─main                                  ——Maven 主目录
├─├─├─java                                  ——java 源代码
├─├─├─├─com.github.mrzhqiang.rowing           ——顶层包
├─├─├─├─├─account                               ——账户
├─├─├─├─├─├─Account                               ——账户实体
├─├─├─├─├─├─AccountController                     ——账户控制器
├─├─├─├─├─├─AccountRepository                     ——账户仓库
├─├─├─├─├─├─AccountService                        ——账户服务
├─├─├─├─├─├─AccountServiceJpaImpl                 ——账户服务 JPA 实现
├─├─├─├─├─action                                ——操作
├─├─├─├─├─config                                ——配置
├─├─├─├─├─dict                                  ——字典
├─├─├─├─├─domain                                ——领域
├─├─├─├─├─exception                             ——异常
├─├─├─├─├─init                                  ——初始化
├─├─├─├─├─menu                                  ——菜单
├─├─├─├─├─monitor                               ——监控
├─├─├─├─├─session                               ——会话
├─├─├─├─├─setting                               ——设置
├─├─├─├─├─system                                ——系统
├─├─├─├─├─third                                 ——第三方
├─├─├─├─├─util                                  ——工具
├─├─├─├─├─RowingApplication                     ——启动类
├─├─├─resources                             ——项目资源
├─├─test                                  ——单元测试
├─web                                   ——前端源码
```

在本项目中，前端保持 `@vue/cli` 创建项目时的初始代码结构，同时参考了 `vue` 官方文档推荐的[代码结构规范][2]。

```
├─web                                   ——前端源码
├─├─public                                ——公共目录，包含可访问的公共资源
├─├─├─index.html                            ——入口页面
├─├─src                                   ——源代码目录
├─├─├─assets                                ——资产
├─├─├─components                            ——组件
├─├─├─App.vue                               ——单页面文件
├─├─├─main.js                               ——执行入口
├─├─package.json                          ——项目依赖
├─├─vue.config.js                         ——项目配置
```

[1]:https://docs.spring.io/spring-boot/docs/2.7.10/reference/html/using.html#using.structuring-your-code.locating-the-main-class

[2]:https://v2.cn.vuejs.org/v2/guide/instance.html#%E5%88%9B%E5%BB%BA%E4%B8%80%E4%B8%AA-Vue-%E5%AE%9E%E4%BE%8B