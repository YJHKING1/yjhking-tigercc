# yjhking-tigercc

## 1、项目简介

金虎云课堂，打造一款在线IT学习云平台，让用户节约时间、金钱成本，摆脱地域和时间的限制，自由享受IT课程学习的服务，用户能在云平台学习免费课程，也可以根据自己需求选择专业课程，付费后学习，用户也能通过论坛版块分享和查看自己及其它用户的提问与文章

## 2、功能架构

- 平台主要业务
    - 管理系统 ： 组织机构管理，角色管理，权限管理，数据字典，系统设置，后台登录
    - 用户中心 ：VIP购买，个人中心，实名认证，资料完善

    - 认证中心 ：统一认证授权中心，前后台用户统一登录

    - 文件管理 ：分布式文件管理中心，基于OSS对象存储

    - 课程中心 ：讲师管理，课程管理，文件上传，课程发布，课程下架

    - 媒体数据 ：视频分片上传，云服务器推流，视频云点播

    - 消息系统 ：短信消息，邮件发送，站内信，系统消息，广告消息

    - 订单中心 ：VIP购买下单，课程购买下单，账户充值下单

    - 支付系统 ：支付宝支付，微信支付

    - 课程秒杀 ：秒杀课程发布，课程秒杀

![image-20220803132710359](https://typora-sz.oss-cn-beijing.aliyuncs.com/uPic/202208031328492.png)

## 3、应用划分

- 项目分为：管理端、门户端、移动端
    - 管理端 : 管理端是后台管理系统，后台管理员，平台工作人员【客服，财务】使用 ， 管理员做平台基础管理工作，平台工作人员做相关业务
        - 比如：数据审核，财务统计
    - 门户端：普通用户通过门户网站进行课程购买和学习，包括登录注册，课程搜索，课程购买，课程学习，社交等等

    - 移动端：提供IOS、Andorid、小程序等让学生可以更加方便地学习，我们放在项目二期实现

![image-20220803132959635](https://typora-sz.oss-cn-beijing.aliyuncs.com/uPic/202208031330916.png)

## 4、项目架构

### 4.1 应用架构

- 项目采用主流的**前后端分离模式**，前端主要有以下两端
    - 系统管理前端
    - 门户前端

![image-20220803133211453](https://typora-sz.oss-cn-beijing.aliyuncs.com/uPic/202208031332494.png)

### 4.2 技术栈

- 系统管理前端采用技术栈：Node.js、Vue.js、Npm、WebPack、Vue Cli 、Element UI 、Easy Mock等等

- 门户网站前端技术栈：Html 、css、js 、jquery等等

- 后端采用微服务架构技术栈

    - 微服务治理：SpringCloud/Alibaba【Eureka/Nacos、Zuul/Gateway、Config/Nacos、Feign、Hystrix/Sentinel、Seluth、Zipkin】+
      MyBatisPlus + SpringBoot + SpringMVC + Velocity
    - 数据存储：Mysql + Redis + Alicoud OSS + ElasticSearch +RocketMQ
    - 运维方面：阿里云服务器，Docker，Jenkins，K8S等等
    - 日志收集【二期】：Logstash + ElasticSearch + Kibana
    - 监控报警【二期】：Metrics + Prometheus + Grafana +Aalertmanager

### 4.3 项目架构说明

- 架构说明
    - 负载层 ： Nginx+lvs
    - 网关 ： Spring Cloud Gateway
    - 服务通信 ： OpenFeign
    - 服务熔断&降级&限流 ：Sentinel
    - 服务发现&配置管理：Nacos
    - 链路追踪 ： Zipkin+Seluth
    - 认证授权：Security+Oauth2+JWT
    - 日志收集: ELK
    - 分布式缓存：Redis
    - 消息队列：RocketMQ
    - 全文搜索：ElasticSearch
    - CI/DI ：jenkins+docker+k8s

![image-20220803133548271](https://typora-sz.oss-cn-beijing.aliyuncs.com/uPic/202208031335350.png)

## 5、项目规划

### 5.1 服务划分

#### 5.1.1 后端服务规划

| 服务名                     | 端口    | 备注   |
|-------------------------|-------|------|
| tigercc-service-gateway | 10010 | 服务网关 |
| tigercc-service-system  | 10021 | 管理中心 |
| tigercc-service-user    | 10030 | 用户中心 |
| tigercc-service-uaa     | 10040 | 认证授权 |
| tigercc-service-course  | 10050 | 课程中心 |
| tigercc-service-file    | 10060 | 媒体数据 |
| tigercc-service-media   | 10070 | 文件管理 |
| tigercc-service-search  | 10080 | 搜索服务 |
| tigercc-service-message | 10090 | 消息服务 |
| tigercc-service-order   | 10100 | 订单中心 |
| tigercc-service-pay     | 10110 | 支付中心 |
| tigercc-service-kill    | 10120 | 秒杀服务 |
| tigercc-service-common  | 10130 | 公共服务 |

#### 5.1.2 前端站点规划

| 服务名               | 端口   | 备注     |
|-------------------|------|--------|
| tigercc-ui-system | 6001 | 系统管理前端 |
| tigercc-ui-course | 6002 | 门户课程站点 |
| tigercc-ui-user   | 6003 | 门户用户站点 |

### 5.2 项目结构

- 项目使用SpringBoot父子聚合工程，搭建完成后需要做如下检查
    - 项目采用pom父子模块进行构建，在一级pom中管理springboot和springcloud依赖以及公共依赖
    - 二级不做pom管理，不导入任何依赖

    - 三级导入项目本身需要的依赖

    - 一级和二级的pom的packaging应该是pom，三级的packaging应该是jar

```
yjhking-tigercc
        ├── yjhking-basic                       // 项目通用，其他项目也可以用的二方库
        │       └── yjhking-basic-common        // 项目通用，其他项目也可以用的公共代码
        │                                       // 公共util,BaseQuery,BaseDomain,PageList,JSONResult
        │       └── yjhking-gen-code            // 项目通用，其他项目也可以用的代码生成器
        ├── tigercc-support                     // 微服务基础服务
        │       └── tigercc-service-gateway     // Gateway网关
        ├── tigercc-basic                       // 基础代码
        │       └── tigercc-basic-common        // 云课堂项目的公共代码,当前项目的工具类 ，实体类
        │       └── tigercc-basic-dependency    // 云课堂微服务项目的公共jar包
        ├── tigercc-service                     // 微服务
        │       └── tigercc-service-system      // 管理服务:controller,service,mapper,mapper.xml
        │       └── tigercc-service-uaa         // 认证授服务
        │       └── tigercc-service-user        // 用户服务
        │       └── tigercc-service-common      // 公共服务
        ├── tigercc-pojo                        // 对象模型
        │       └── tigercc-pojo-system         // 管理服务对象模型:Domain,Dto,Vo,To,SystemQuery
        │       └── tigercc-pojo-uaa            // 认证服务的对象模型
        │       └── tigercc-pojo-user           // 用户服务的对象模型
        │       └── tigercc-pojo-common         // 公共服务的对象模型
        ├── tigercc-api                         // 服务的API，使用Feign通信
        │       └──tigercc-api-system           // 管理服务的API:调用system服务的Feign接口
        │       └──tigercc-api-uaa              // 认证服务API
        ├── tigercc-ui                          // 前端站点
        │       └── tigercc-ui-system           // 管理系统
        │       └── tigercc-ui-course           // 门户-课程
        │       └── tigercc-ui-user             // 门户-用户
        ├──pom.xml                              // 管理公共依赖以及SpringBoot,SpringCloud
```