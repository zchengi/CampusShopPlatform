CREATE TABLE `tb_shop_category` (
  `shop_category_id`   INT(11)       NOT NULL  AUTO_INCREMENT,
  `shop_category_name` VARCHAR(100)  NOT NULL  DEFAULT '',
  `shop_category_desc` VARCHAR(1000) NOT NULL  DEFAULT '',
  `shop_category_img`  VARCHAR(2000)           DEFAULT NULL,
  `priority`           INT(2)        NOT NULL  DEFAULT 0,
  `create_time`        DATETIME                DEFAULT NULL,
  `last_edit_time`     DATETIME                DEFAULT NULL,
  `parent_id`          INT(11)                 DEFAULT NULL,
  PRIMARY KEY (shop_category_id),
  CONSTRAINT `fk_shop_category_self` FOREIGN KEY (parent_id) REFERENCES tb_shop_category (shop_category_id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;