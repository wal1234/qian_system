# Docker 微服务架构部署指南

## 📋 概述

本项目提供了完整的 Docker 微服务架构部署方案，所有 Docker 相关文件已统一整理到 `docker/` 目录下。

## 🚀 快速部署

### 方式一：一键部署（推荐）

```bash
# 进入 docker 目录
cd docker

# Windows 一键部署
deploy.bat

# Linux/macOS 一键部署
chmod +x deploy.sh
./deploy.sh

# 或者使用原有的启动脚本
# Windows
scripts\start.bat

# Linux/macOS
chmod +x scripts/start.sh
./scripts/start.sh
```

### 方式二：手动部署

```bash
# 1. 进入 docker 目录
cd docker

# 2. 创建目录结构
# Windows
scripts\setup-directories.bat

# Linux/macOS
chmod +x scripts/setup-directories.sh
./scripts/setup-directories.sh

# 3. 启动服务
docker-compose up -d

# 4. 检查服务状态
# Windows
scripts\check-services.bat

# Linux/macOS
chmod +x scripts/check-services.sh
./scripts/check-services.sh
```

## 📁 目录结构

```
docker/                                 # Docker 部署目录
├── README.md                          # 主要部署文档
├── docker-compose.yml                 # Docker Compose 配置
├── deploy.bat                         # 一键部署脚本
├── docs/                              # 详细文档
│   ├── README-Docker.md               # 详细部署指南
│   ├── DOCKER-COMPOSE-OPTIMIZATION.md # 配置优化说明
│   ├── TOMCAT-JDK17-UPGRADE.md       # Tomcat JDK17 升级说明
│   ├── NACOS-AUTH-CONFIG.md          # Nacos 鉴权配置
│   └── FILES-LIST.md                 # 文件清单
├── scripts/                           # 管理脚本
│   ├── setup-directories.bat/.sh     # 目录初始化
│   ├── start.bat/.sh                 # 启动脚本
│   ├── stop.bat/.sh                  # 停止脚本
│   ├── check-services.bat/.sh        # 服务检查
│   ├── install-docker.sh             # Docker 安装脚本 (Linux)
│   └── generate-nacos-keys.bat       # 密钥生成
└── services/                          # 服务配置
    ├── mysql/                         # MySQL 配置和数据
    ├── nacos/                         # Nacos 配置和数据
    ├── redis/                         # Redis 配置和数据
    ├── tomcat/                        # Tomcat 配置和应用
    ├── nginx/                         # Nginx 配置和静态文件
    └── portainer/                     # Portainer 数据
```

## 🏗️ 架构组件

- **MySQL 8.0.39** - 数据库服务
- **Nacos 2.3.2** - 服务注册与配置中心
- **Redis 6.2** - 缓存服务
- **Tomcat 10-JDK17** - 应用服务器
- **Nginx 1.26** - 反向代理和负载均衡
- **Adminer 4.8.1** - 数据库管理工具
- **Portainer 2.21.0** - 容器监控管理

## 🌐 服务访问

| 服务 | 地址 | 用户名 | 密码 |
|------|------|--------|------|
| 主页 | http://localhost | - | - |
| Nacos 控制台 | http://localhost/nacos | nacos | kzt10202612 |
| Adminer | http://localhost:8888 | nacos | kzt10202612 |
| Portainer | http://localhost:9000 | admin | 自定义 |
| Tomcat | http://localhost:8080 | - | - |
| MySQL | localhost:3306 | nacos | kzt10202612 |
| Redis | localhost:6379 | - | kzt10202612 |

## 📚 详细文档

- **[主要部署文档](docker/README.md)** - 完整的部署指南
- **[Linux 部署指南](docker/docs/LINUX-DEPLOYMENT.md)** - Linux 系统专用部署文档
- **[详细配置说明](docker/docs/README-Docker.md)** - 深入的配置解释
- **[优化说明](docker/docs/DOCKER-COMPOSE-OPTIMIZATION.md)** - 性能优化详解
- **[JDK17 升级](docker/docs/TOMCAT-JDK17-UPGRADE.md)** - Tomcat JDK17 升级指南
- **[Nacos 鉴权](docker/docs/NACOS-AUTH-CONFIG.md)** - Nacos 安全配置

## 🔧 常用命令

```bash
# 进入 docker 目录
cd docker

# 查看服务状态
docker-compose ps

# 查看服务日志
docker-compose logs -f [服务名]

# 重启服务
docker-compose restart [服务名]

# 停止所有服务
docker-compose down

# 完全清理（包括数据）
docker-compose down -v
```

## 🛠️ 故障排除

1. **查看详细日志**
   ```bash
   cd docker
   docker-compose logs
   ```

2. **检查服务健康**
   ```bash
   cd docker
   scripts\check-services.bat
   ```

3. **重新部署**
   ```bash
   cd docker
   docker-compose down
   docker-compose up -d --force-recreate
   ```

## 💡 注意事项

- **首次部署**: 可能需要 2-3 分钟完成所有服务初始化
- **端口冲突**: 确保 80、3306、6379、8080、8848、8888、9000 端口未被占用
- **内存要求**: 建议至少 4GB 可用内存
- **磁盘空间**: 建议至少 10GB 可用空间
- **生产环境**: 请修改默认密码并启用 SSL

## 🔒 安全建议

1. **修改默认密码**
2. **启用 SSL/TLS**
3. **配置防火墙规则**
4. **定期备份数据**
5. **监控服务状态**

---

**快速开始**: `cd docker && deploy.bat` 🚀 