CREATE TABLE `tb_product_img` (
  `product_img_id` INT(20)       NOT NULL AUTO_INCREMENT,
  `img_addr`       VARCHAR(2000) NOT NULL,
  `img_desc`       VARCHAR(2000)          DEFAULT NULL,
  `priority`       INT(2)                 DEFAULT 0,
  `create_time`    DATETIME               DEFAULT NULL,
  `product_id`     INT(20)                DEFAULT NULL,
  PRIMARY KEY (product_img_id),
  CONSTRAINT `fk_product_img_product` FOREIGN KEY (product_id) REFERENCES tb_product (product_id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;