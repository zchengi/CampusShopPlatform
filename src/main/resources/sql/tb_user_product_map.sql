CREATE TABLE `tb_user_product_map` (
  `user_product_id` INT(30) NOT NULL AUTO_INCREMENT,
  `user_id`         INT(10)          DEFAULT NULL,
  `product_id`      INT(100)         DEFAULT NULL,
  `shop_id`         INT(10)          DEFAULT NULL,
  `operator_id`     INT(10)          DEFAULT NULL,
  `create_time`     DATETIME         DEFAULT NULL,
  `point`           INT(10)          DEFAULT '0',
  PRIMARY KEY (`user_product_id`),
  KEY `fk_user_product_map_profile` (`user_id`),
  KEY `fk_user_product_map_product` (`product_id`),
  KEY `fk_user_product_map_shop` (`shop_id`),
  CONSTRAINT `fk_user_product_map_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`),
  CONSTRAINT `fk_user_product_map_product` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`),
  CONSTRAINT `fk_user_product_map_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
