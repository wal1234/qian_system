# Qian System 项目架构文档

## 1. 项目概述

Qian System 是一个基于 Spring Cloud 2021.0.4.0 的微服务系统，采用 Nacos 作为配置中心和注册中心，使用 MySQL 作为数据库。

## 2. 技术栈

- 框架：Spring Boot、Spring Cloud 2021.0.4.0
- 注册中心/配置中心：Nacos
- 负载均衡：Spring Cloud LoadBalancer
- 熔断器：Resilience4j
- 远程调用：OpenFeign
- 数据库：MySQL
- 连接池：Druid
- ORM：MyBatis
- 监控：Spring Boot Actuator、Prometheus
- 安全：Spring Security
- 日志：Logback
- API文档：SpringDoc OpenAPI (替代Swagger)

## 3. 项目结构

```
qian_system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/qian/
│   │   │       ├── config/        # 配置类
│   │   │       ├── controller/    # 控制器
│   │   │       ├── service/       # 服务层
│   │   │       ├── mapper/        # MyBatis映射
│   │   │       └── domain/        # 实体类
│   │   └── resources/
│   │       ├── mapper/           # MyBatis XML映射文件
│   │       ├── mybatis/          # MyBatis配置
│   │       ├── application-common.yml    # 通用配置
│   │       ├── application-prod.yml      # 生产环境配置
│   │       └── bootstrap.yml             # 启动配置
│   └── test/                     # 测试代码
└── pom.xml                      # 项目依赖
```

## 4. 配置说明

### 4.1 配置文件分层

- `bootstrap.yml`：基础配置，最先加载
  - Nacos 配置
  - 服务器配置
  - 监控配置

- `application-common.yml`：通用配置
  - Spring Cloud 组件配置
    - LoadBalancer
    - Resilience4j
    - OpenFeign
  - 数据库配置
  - MyBatis 配置
  - 日志配置

- `application-prod.yml`：生产环境配置
  - 生产环境特定配置
  - 安全配置
  - 性能优化配置
  - 熔断器增强配置

### 4.2 环境变量配置

#### 数据库配置
```bash
MYSQL_HOST=your_host
MYSQL_PORT=3306
MYSQL_DATABASE=qian_blog
MYSQL_USERNAME=your_username
MYSQL_PASSWORD=your_password
```

#### Nacos配置
```bash
NACOS_SERVER_ADDR=your_nacos_addr
NACOS_NAMESPACE=your_namespace
NACOS_USERNAME=your_username
NACOS_PASSWORD=your_password
```

#### 安全配置
```bash
DRUID_PASSWORD=your_druid_password
SECURITY_PASSWORD=your_security_password
JWT_JWK_URI=your_jwk_uri
```

#### 日志配置
```bash
LOG_PATH=/var/logs/qian-system
LOG_LEVEL_APP=info
```

## 5. 功能特性

### 5.1 监控功能

- 健康检查：`/actuator/health`
- 指标监控：`/actuator/metrics`
- 日志级别：`/actuator/loggers`
- 环境信息：`/actuator/env`
- Druid监控：`/druid/*`
- Prometheus：`/actuator/prometheus`

### 5.2 安全特性

- 基本认证
- OAuth2/JWT支持
- SSL支持
- 防火墙规则
- 敏感操作限制
- IP访问控制

### 5.3 性能优化

- 连接池优化
- 日志优化
- 压缩配置
- 缓存配置
- LoadBalancer缓存
- 批处理优化

### 5.4 微服务特性

- 服务发现与注册
- 负载均衡
- 熔断降级
- 限流控制
- 重试机制
- 舱壁隔离

## 6. 部署说明

### 6.1 环境要求

- JDK 1.8+
- MySQL 5.7+
- Nacos 2.0+
- Maven 3.6+

### 6.2 部署步骤

1. 配置环境变量
```bash
# 设置必要的环境变量
export MYSQL_HOST=your_host
export MYSQL_PASSWORD=your_password
# ... 其他环境变量
```

2. 创建日志目录
```bash
mkdir -p /var/logs/qian-system
chmod 755 /var/logs/qian-system
```

3. 启动应用
```bash
java -jar qian-system.jar
```

### 6.3 生产环境检查清单

- [ ] 所有敏感信息通过环境变量配置
- [ ] 日志目录权限正确
- [ ] 防火墙规则配置
- [ ] SSL证书配置
- [ ] 监控系统配置
- [ ] 数据库备份策略
- [ ] 安全策略配置
- [ ] 熔断器配置正确
- [ ] 限流配置正确

## 7. 开发规范

### 7.1 代码规范

- 遵循阿里巴巴Java开发手册
- 使用统一的代码格式化工具
- 编写单元测试
- 代码审查
- 使用熔断器保护远程调用
- 添加适当的超时设置

### 7.2 配置规范

- 敏感信息使用环境变量
- 配置文件分层管理
- 使用配置中心管理配置
- 定期更新密码
- 合理设置熔断器参数

### 7.3 日志规范

- 使用统一的日志格式
- 区分不同级别的日志
- 配置日志滚动策略
- 定期清理日志
- 日志中包含足够的上下文信息

## 8. 维护说明

### 8.1 日常维护

- 监控系统状态
- 检查日志文件
- 数据库维护
- 安全更新
- 熔断器状态监控

### 8.2 故障处理

- 查看应用日志
- 检查监控指标
- 数据库连接检查
- 网络连接检查
- 熔断器状态检查
- 检查微服务间调用状态

### 8.3 性能优化

- 定期检查性能指标
- 优化数据库查询
- 调整连接池参数
- 优化缓存策略
- 调整负载均衡策略
- 优化熔断器配置

## 9. 安全建议

1. 定期更改密码
2. 限制监控页面访问IP
3. 使用强密码
4. 启用SSL
5. 配置防火墙规则
6. 定期安全审计
7. 及时更新安全补丁
8. 使用OAuth2/JWT增强安全性
9. 防止敏感信息泄露

## 10. Spring Cloud 2021 特性使用说明

### 10.1 负载均衡

Spring Cloud 2021 使用 Spring Cloud LoadBalancer 替代了 Ribbon，配置示例：

```yaml
spring:
  cloud:
    loadbalancer:
      cache:
        enabled: true
        caffeine:
          spec: expireAfterWrite=5s
```

### 10.2 熔断器

Spring Cloud 2021 使用 Resilience4j 替代了 Hystrix，配置示例：

```yaml
resilience4j:
  circuitbreaker:
    configs:
      default:
        slidingWindowSize: 10
        failureRateThreshold: 50
        waitDurationInOpenState: 5s
```

### 10.3 服务调用

Spring Cloud 2021 使用 OpenFeign 进行服务调用，配置示例：

```yaml
feign:
  circuitbreaker:
    enabled: true
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
```

## 11. 联系方式

如有问题，请联系系统管理员。 