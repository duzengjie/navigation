CREATE DATABASE `navigation` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- navigation.environment_info definition

CREATE TABLE `environment_info` (
                                    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                    `name` varchar(20) NOT NULL COMMENT '环境名称',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='环境信息';


-- navigation.url_info definition

CREATE TABLE `url_info` (
                            `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `environment_id` int(11) NOT NULL COMMENT '环境 id',
                            `url` varchar(255) NOT NULL COMMENT '地址',
                            `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `url_name` varchar(100) NOT NULL COMMENT 'url名称',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='链接信息';

-- 01.08 新增
ALTER TABLE navigation.url_info ADD order_num int NULL COMMENT '排序,数字越小展示越靠前';
ALTER TABLE navigation.url_info CHANGE order_num order_num int NULL COMMENT '排序,数字越小展示越靠前' AFTER remark;
-- navigation.url_info_change_log definition
CREATE TABLE `url_info_change_log` (
                                       `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                       `original_id` int NOT NULL COMMENT '原 id',
                                       `environment_id` int NOT NULL COMMENT '环境 id',
                                       `url_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'url名称',
                                       `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址',
                                       `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
                                       `order_num` int DEFAULT NULL COMMENT '排序,数字越小展示越靠前',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='链接信息修改记录';