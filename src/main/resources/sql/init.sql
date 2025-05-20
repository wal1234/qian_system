-- ----------------------------
-- 创建数据库
-- ----------------------------
CREATE DATABASE IF NOT EXISTS `qian_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `qian_system`;

-- ----------------------------
-- 表结构
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `email` varchar(100) DEFAULT NULL COMMENT '用户邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '帐号状态（0停用 1正常）',
  `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `login_ip` varchar(128) DEFAULT NULL COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 文章表
CREATE TABLE `blog_article` (
    `article_id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `title` varchar(200) NOT NULL COMMENT '文章标题',
    `content` longtext NOT NULL COMMENT '文章内容',
    `summary` varchar(500) DEFAULT NULL COMMENT '文章摘要',
    `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片',
    `category_id` bigint DEFAULT NULL COMMENT '分类ID',
    `view_count` int NOT NULL DEFAULT '0' COMMENT '浏览量',
    `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数',
    `comment_count` int NOT NULL DEFAULT '0' COMMENT '评论数',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0草稿 1发布）',
    `is_top` tinyint NOT NULL DEFAULT '0' COMMENT '是否置顶（0否 1是）',
    `is_recommend` tinyint NOT NULL DEFAULT '0' COMMENT '是否推荐（0否 1是）',
    `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`article_id`),
    KEY `idx_category` (`category_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 文章分类表
CREATE TABLE `blog_category` (
    `category_id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` varchar(50) NOT NULL COMMENT '分类名称',
    `description` varchar(200) DEFAULT NULL COMMENT '分类描述',
    `sort` int NOT NULL DEFAULT '0' COMMENT '显示顺序',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0停用 1正常）',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='文章分类表';

-- 文章标签表
CREATE TABLE `blog_tag` (
    `tag_id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `name` varchar(50) NOT NULL COMMENT '标签名称',
    `description` varchar(200) DEFAULT NULL COMMENT '标签描述',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0停用 1正常）',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='文章标签表';

-- 文章标签关联表
CREATE TABLE `blog_article_tag` (
    `article_id` bigint NOT NULL COMMENT '文章ID',
    `tag_id` bigint NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`article_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章标签关联表';

-- 评论表
CREATE TABLE `blog_comment` (
    `comment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `article_id` bigint NOT NULL COMMENT '文章ID',
    `parent_id` bigint DEFAULT NULL COMMENT '父评论ID',
    `user_id` bigint NOT NULL COMMENT '评论用户ID',
    `content` varchar(1000) NOT NULL COMMENT '评论内容',
    `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0隐藏 1显示）',
    `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`comment_id`),
    KEY `idx_article` (`article_id`),
    KEY `idx_parent` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 用户收藏表
CREATE TABLE `blog_favorite` (
    `favorite_id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `article_id` bigint NOT NULL COMMENT '文章ID',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`favorite_id`),
    UNIQUE KEY `idx_user_article` (`user_id`,`article_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- 用户点赞表
CREATE TABLE `blog_like` (
    `like_id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `target_id` bigint NOT NULL COMMENT '目标ID',
    `type` tinyint NOT NULL COMMENT '类型（1文章 2评论）',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`like_id`),
    UNIQUE KEY `idx_user_target` (`user_id`,`target_id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户点赞表';

-- 初始化管理员账号
INSERT INTO `sys_user` (`username`, `nickname`, `password`, `status`, `create_time`, `update_time`, `remark`)
VALUES ('admin', '管理员', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 1, NOW(), NOW(), '管理员账号');

-- 初始化默认分类
INSERT INTO `blog_category` (`name`, `description`, `sort`, `status`, `create_time`, `update_time`)
VALUES ('默认分类', '默认文章分类', 1, 1, NOW(), NOW());

-- 初始化默认标签
INSERT INTO `blog_tag` (`name`, `description`, `status`, `create_time`, `update_time`)
VALUES ('默认标签', '默认文章标签', 1, NOW(), NOW()); 