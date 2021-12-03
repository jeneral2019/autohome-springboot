-- create database
drop database if exists autohome;
create database autohome;

-- create user
create user autohome IDENTIFIED BY 'password';
use mysql;
ALTER USER 'autohome'@'%' IDENTIFIED WITH mysql_native_password BY 'autohomeTest';

grant all privileges on autohome.* to autohome;

flush privileges;

-- init tables
drop table if exists `autohome`.`autohome_spider`;
CREATE TABLE `autohome`.`autohome_spider` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `spec_id` int DEFAULT NULL COMMENT '汽车之家ID',
  `level` smallint DEFAULT NULL COMMENT '层级',
  `parent_id` int DEFAULT NULL COMMENT '父级spec_id',
  `first_letter` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '首字母',
  `brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '品牌',
  `series` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '系列',
  `year` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '年款',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '车型数据logo',
  `car_manufacturer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '厂商',
  `car_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '车辆类型',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '车型详情路径',
  `engine` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发动机',
  `precursor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '前置四驱',
  `gear` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '离合档位',
  `status`int DEFAULT NULL COMMENT '0为开始爬虫,1正在爬虫,2爬虫完成,20爬虫成功但是没有3级数据,500有错误数据',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_spec_id` (`spec_id`),
  KEY `idx_first_char` (`first_letter`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;