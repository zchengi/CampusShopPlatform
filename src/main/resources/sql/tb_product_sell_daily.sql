CREATE TABLE `tb_product_sell_daily` (
  `product_id`  INT(100) DEFAULT NULL,
  `shop_id`     INT(10)  DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `total`       INT(10)  DEFAULT '0',
  KEY `fk_product_sell_product` (`product_id`),
  KEY `fk_product_sell_shop` (`shop_id`),
  CONSTRAINT `fk_product_sell_product` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`),
  CONSTRAINT `fk_product_sell_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;