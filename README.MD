## 功能模块
系统功能模块组成如下所示：
```
├─系统管理
│  ├─菜单管理 --完成
│  ├─部门管理
│  ├─用户管理
│  ├─角色管理
│  ├─字典管理 --完成
│  └─配置管理
│      ├─邮箱配置
├─系统监控
│  ├─在线用户 --完成
│  └─系统日志 --完成

```
关于系统
````
本是想使用Material Design作为前端风格，springboot作为后端框架，所以取名MUIBOOT；
但是，市面上的Material Design风格前端组件基本不兼容IE8及以下，所以前端选择了兼容性
较好的layui.后台框架实在FEBS基础上修改，站在巨人的肩上。
````
## 技术选型
### 后端

- 基础框架：Spring Boot

- 持久层框架：Mybatis

- 安全框架：Apache Shiro

- 摸板引擎：Thymeleaf

- 数据库连接池：阿里巴巴Druid

- 缓存框架：Ehcache

- 日志打印：logback

- 其他：fastjson，poi，javacsv等。

### 前端
 
- 基础框架：layiui

- JavaScript框架：jQuery

### 开发环境

- 语言：Java

- IDE：IDEA

- 依赖管理：Maven

- 数据库：MYSQL

- 版本管理：git
### 参考项目
[FEBS](https://gitee.com/github-16661027/project)权限管理系统