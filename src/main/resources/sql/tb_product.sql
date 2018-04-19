CREATE TABLE `tb_product` (
  `product_id`          INT(100)     NOT NULL  AUTO_INCREMENT,
  `product_name`        VARCHAR(100) NOT NULL,
  `product_desc`        VARCHAR(2000)          DEFAULT NULL,
  `img_addr`            VARCHAR(2000)          DEFAULT '',
  `normal_price`       VARCHAR(100)           DEFAULT NULL,
  `promotion_price`     VARCHAR(100)           DEFAULT NULL,
  `priority`            INT(2)       NOT NULL  DEFAULT 0,
  `create_time`         DATETIME               DEFAULT NULL,
  `last_edit_time`      DATETIME               DEFAULT NULL,
  `enable_status`       INT(2)       NOT NULL  DEFAULT 0,
  `product_category_id` INT(11)                DEFAULT NULL,
  `shop_id`             INT(20)      NOT NULL  DEFAULT 0,
  PRIMARY KEY (product_id),
  CONSTRAINT `fk_product_product_category` FOREIGN KEY (product_category_id) REFERENCES tb_product_category (product_category_id),
  CONSTRAINT `fk_product_shop` FOREIGN KEY (shop_id) REFERENCES tb_shop (shop_id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

# 新增字段 point
# `point` INT(10) DEFAULT '0'