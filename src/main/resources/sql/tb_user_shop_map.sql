CREATE TABLE `tb_user_shop_map` (
  `user_shop_id` INT(30) NOT NULL AUTO_INCREMENT,
  `user_id`      INT(10) NOT NULL,
  `shop_id`      INT(10) NOT NULL,
  `create_time`  DATETIME         DEFAULT NULL,
  `point`        INT(10)          DEFAULT NULL,
  PRIMARY KEY (`user_shop_id`),
  UNIQUE KEY `uq_user_shop` (`user_id`, `shop_id`),
  KEY `fk_user_shop_shop` (`shop_id`),
  CONSTRAINT `fk_user_shop_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`),
  CONSTRAINT `fk_user_shop_user` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;