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