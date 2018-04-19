CREATE TABLE `tb_award` (
  `award_id`       INT(10)                              NOT NULL  AUTO_INCREMENT,
  `award_name`     VARCHAR(256) COLLATE utf8_unicode_ci NOT NULL,
  `award_desc`     VARCHAR(1024) COLLATE utf8_unicode_ci          DEFAULT NULL,
  `award_img`      VARCHAR(1024) COLLATE utf8_unicode_ci          DEFAULT NULL,
  `point`          INT(10)                              NOT NULL  DEFAULT '0',
  `priority`       INT(2)                               NOT NULL  DEFAULT '0',
  `create_time`    DATETIME                                       DEFAULT NULL,
  `last_edit_time` DATETIME                                       DEFAULT NULL,
  `enable_status`  INT(2)                               NOT NULL  DEFAULT '0',
  `shop_id`        INT(10)                                        DEFAULT NULL,
  PRIMARY KEY (`award_id`),
  KEY `fk_award_shop_idx` (`shop_id`),
  CONSTRAINT `fk_award_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;