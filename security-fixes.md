# 依赖安全漏洞修复指南

## 发现的安全漏洞

在项目中发现了以下安全漏洞：

### 1. Guava 20.0 版本漏洞

**问题描述**：项目中存在 `com.google.guava:guava:20.0` 传递性依赖，该版本存在以下已知安全漏洞：

- **CVE-2018-10237**：在并发哈希表实现中存在拒绝服务漏洞
- **CVE-2020-8908**：可能导致路径遍历漏洞
- **GHSA-5mg8-w23w-74h3**：Java序列化漏洞

**已修复**：更新到 `32.1.2-jre` 版本

### 2. Spring Boot 2.7.12 漏洞

**问题描述**：Spring Boot 2.7.12 版本存在以下安全漏洞：

- **CVE-2023-20883**：Spring Boot 中的目录遍历漏洞
- **CVE-2023-20860**：Spring Expression 存在远程代码执行漏洞

**已修复**：更新到 `2.7.14` 版本

### 3. snakeyaml 漏洞

**问题描述**：项目依赖的 snakeyaml 版本存在多个严重漏洞：

- **CVE-2022-25857**：反序列化漏洞
- **CVE-2022-38749**：RCE 漏洞
- **CVE-2022-38750**：另一个 RCE 漏洞
- **CVE-2022-1471**：允许反序列化导致 RCE

**已修复**：更新到 `2.0` 版本并显式管理该依赖

### 4. MySQL Connector/J 8.0.31 漏洞

**问题描述**：MySQL Connector/J 8.0.31 存在以下漏洞：

- **CVE-2023-21971**：认证绕过漏洞
- **CVE-2022-21363**：另一个安全漏洞

**已修复**：更新到 `8.0.33` 版本，并更新 GAV 坐标到 `com.mysql:mysql-connector-j`

### 5. Netty 相关漏洞

**问题描述**：Netty 的传递依赖存在多个漏洞：

- **CVE-2023-4586**：Netty HTTP/2 DoS 漏洞
- **CVE-2023-34462**：HTTP/2 解码器放大攻击漏洞

**已修复**：在 dependencyManagement 中添加 Netty 4.1.94.Final 版本

### 6. Tomcat 漏洞

**问题描述**：嵌入式 Tomcat 存在多个漏洞：

- **CVE-2023-28708**：验证绕过漏洞
- **CVE-2023-34981**：HTTP 请求走私漏洞

**已修复**：在 dependencyManagement 中添加 Tomcat 9.0.78 版本

### 7. Spring Cloud 2021 配置不兼容问题

**问题描述**：Spring Cloud 2021 版本中，配置加载机制发生了变化，要求显式指定 `spring.config.import` 属性。

**错误信息**：
```
No spring.config.import property has been defined
```

**已修复**：在 bootstrap.yml 和 application.yml 中添加 `spring.config.import` 配置

## 解决方案

### 1. 强制使用安全版本的依赖

我们已经更新了 `pom.xml` 文件中的依赖版本：

```xml
<properties>
    <java.version>17</java.version>
    <spring-cloud.version>2021.0.8</spring-cloud.version>
    <spring-cloud-alibaba.version>2021.0.5.0</spring-cloud-alibaba.version>
    <mybatis.version>2.3.1</mybatis.version>
    <mysql.version>8.0.33</mysql.version>
    <druid.version>1.2.18</druid.version>
    <guava.version>32.1.2-jre</guava.version>
    <jackson.version>2.15.2</jackson.version>
    <log4j2.version>2.20.0</log4j2.version>
    <snakeyaml.version>2.0</snakeyaml.version>
    <tomcat.version>9.0.78</tomcat.version>
    <netty.version>4.1.94.Final</netty.version>
</properties>
```

### 2. Spring Cloud 2021 配置修复

在 Spring Cloud 2021 版本中，配置加载机制发生了变化，需要在配置文件中显式指定配置来源：

**bootstrap.yml：**
```yaml
spring:
  config:
    import: optional:nacos:${spring.application.name}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
```

**application.yml：**
```yaml
spring:
  config:
    import: optional:nacos:${spring.application.name}.${spring.cloud.nacos.config.file-extension}
```

这是 Spring Cloud 2021 版本的一个重要变化，`extension-configs` 和 `shared-configs` 仍然可以使用，但必须搭配 `spring.config.import` 属性。

如果不想使用 `spring.config.import`，也可以禁用检查（不推荐）：
```yaml
spring:
  cloud:
    nacos:
      config:
        import-check:
          enabled: false
```

### 3. snakeyaml 特殊处理

由于 snakeyaml 漏洞特别严重，我们需要特殊处理：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.yaml</groupId>
            <artifactId>snakeyaml</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.yaml</groupId>
    <artifactId>snakeyaml</artifactId>
    <version>${snakeyaml.version}</version>
</dependency>
```

### 4. 在依赖管理中强制使用安全版本

添加或更新了 dependencyManagement 部分，确保传递依赖也使用安全版本：

```xml
<dependencyManagement>
    <dependencies>
        <!-- 其他依赖管理 -->
        
        <!-- 解决漏洞依赖 -->
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>${guava.version}</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        <dependency>
            <groupId>org.yaml</groupId>
            <artifactId>snakeyaml</artifactId>
            <version>${snakeyaml.version}</version>
        </dependency>
        <!-- Tomcat 相关组件 -->
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-core</artifactId>
            <version>${tomcat.version}</version>
        </dependency>
        <!-- Netty 相关组件 -->
        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-handler</artifactId>
            <version>${netty.version}</version>
        </dependency>
        <!-- 更多组件... -->
    </dependencies>
</dependencyManagement>
```

### 5. 添加版本检查插件

添加了 versions-maven-plugin 插件，帮助定期检查依赖更新：

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>versions-maven-plugin</artifactId>
    <version>2.15.0</version>
    <configuration>
        <rulesUri>file:///${project.basedir}/version-rules.xml</rulesUri>
    </configuration>
</plugin>
```

并创建了 `version-rules.xml` 文件，用于控制版本更新规则。

### 6. 升级依赖检查工具

更新了 OWASP 依赖检查插件到最新版本：

```xml
<plugin>
    <groupId>org.owasp</groupId>
    <artifactId>dependency-check-maven</artifactId>
    <version>8.2.1</version>
    <configuration>
        <failBuildOnCVSS>8</failBuildOnCVSS>
        <suppressionFiles>
            <suppressionFile>owasp-suppressions.xml</suppressionFile>
        </suppressionFiles>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## 持续安全最佳实践

1. 定期运行依赖更新检查：
   ```bash
   mvn versions:display-dependency-updates
   ```

2. 定期运行漏洞检查：
   ```bash
   mvn dependency-check:check
   ```

3. 构建时自动检查漏洞：
   ```bash
   mvn clean verify
   ```

4. 订阅 CVE 漏洞警报，特别关注以下组件：
   - Spring Boot
   - Spring Cloud
   - Spring Framework
   - Netty
   - Tomcat
   - Jackson
   - MySQL Connector
   - snakeyaml

5. 至少每季度更新一次所有依赖的版本 