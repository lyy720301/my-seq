-- 需要使用MyISAM引擎

-- 创建数据库
CREATE DATABASE IF NOT EXISTS seq CHARACTER SET utf8 COLLATE utf8_general_ci;

-- 使用数据库
USE seq;

-- 删除表（如果存在）
DROP TABLE IF EXISTS `demo_seq`;
CREATE TABLE `demo_seq`
(
    `id`   bigint UNSIGNED NOT NULL AUTO_INCREMENT,
    `stub` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `stub`(`stub`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'demo_seq' ROW_FORMAT = Fixed;

