CREATE DATABASE `navigation` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

/*
 Navicat Premium Dump SQL

 Source Server         : 127.0.0.1·duzengjie
 Source Server Type    : MySQL
 Source Server Version : 80300 (8.3.0)
 Source Host           : 127.0.0.1:3306
 Source Schema         : navigation

 Target Server Type    : MySQL
 Target Server Version : 80300 (8.3.0)
 File Encoding         : 65001

 Date: 15/09/2024 20:28:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for environment_info
-- ----------------------------
DROP TABLE IF EXISTS `environment_info`;
CREATE TABLE `environment_info`  (
                                     `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                     `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '环境名称',
                                     `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '环境信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for url_info
-- ----------------------------
DROP TABLE IF EXISTS `url_info`;
CREATE TABLE `url_info`  (
                             `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                             `environment_id` int NOT NULL COMMENT '环境 id',
                             `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址',
                             `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
                             `order_num` int NULL DEFAULT NULL COMMENT '排序,数字越小展示越靠前',
                             `use_num` int NULL DEFAULT NULL COMMENT '使用次数',
                             `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                             `url_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'url名称',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '链接信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for url_info_change_log
-- ----------------------------
DROP TABLE IF EXISTS `url_info_change_log`;
CREATE TABLE `url_info_change_log`  (
                                        `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                        `original_id` int NOT NULL COMMENT '原 id',
                                        `environment_id` int NOT NULL COMMENT '环境 id',
                                        `url_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'url名称',
                                        `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址',
                                        `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
                                        `order_num` int NULL DEFAULT NULL COMMENT '排序,数字越小展示越靠前',
                                        `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '链接信息修改记录' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

