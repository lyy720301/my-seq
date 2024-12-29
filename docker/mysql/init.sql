CREATE DATABASE IF NOT EXISTS seq;
USE seq;

-- Create sequence table
CREATE TABLE IF NOT EXISTS `video_seq` (
    `id`   bigint UNSIGNED NOT NULL AUTO_INCREMENT,
    `stub` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `stub`(`stub`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'video_seq' ROW_FORMAT = Fixed;

CREATE TABLE IF NOT EXISTS `shop_seq` (
   `id`   bigint UNSIGNED NOT NULL AUTO_INCREMENT,
   `stub` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `stub`(`stub`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'shop_seq' ROW_FORMAT = Fixed;