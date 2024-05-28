-- 需要使用MyISAM引擎

DROP TABLE IF EXISTS `video_seq`;
CREATE TABLE `video_seq`
(
    `id`   bigint UNSIGNED NOT NULL AUTO_INCREMENT,
    `stub` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `stub`(`stub`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'video_seq' ROW_FORMAT = Fixed;

