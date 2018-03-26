CREATE TABLE `tb_product_category` (
  `product_category_id`   INT(11)      NOT NULL  AUTO_INCREMENT,
  `product_category_name` VARCHAR(100) NOT NULL,
  `priority`              INT(2)                 DEFAULT 0,
  `create_time`           DATETIME               DEFAULT NULL,
  `shop_id`               INT(20)      NOT NULL  DEFAULT 0,
  PRIMARY KEY (product_category_id),
  CONSTRAINT `fk_product_category_shop` FOREIGN KEY (shop_id) REFERENCES `tb_shop` (shop_id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;