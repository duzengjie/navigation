-- navigation.environment_info definition

CREATE TABLE `environment_info` (
                                    `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                    `name` varchar(20) NOT NULL COMMENT '环境名称',
                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                    `update_time` datetime NOT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='环境信息';


-- navigation.url_info definition

CREATE TABLE `url_info` (
                            `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `environment_id` int NOT NULL COMMENT '环境 id',
                            `url` varchar(255) NOT NULL COMMENT '地址',
                            `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                            `create_time` datetime NOT NULL COMMENT '创建时间',
                            `update_time` datetime NOT NULL COMMENT '更新时间',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='链接信息';