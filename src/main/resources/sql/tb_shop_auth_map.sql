CREATE TABLE `tb_shop_auth_map` (
  `shop_auth_id`   INT(10) NOT NULL                    AUTO_INCREMENT,
  `employee_id`    INT(10) NOT NULL,
  `shop_id`        INT(10) NOT NULL,
  `title`          VARCHAR(255)COLLATE utf8_unicode_ci DEFAULT NULL,
  `title_flag`     INT(2)                              DEFAULT NULL,
  `create_time`    DATETIME                            DEFAULT NULL,
  `last_edit_time` DATETIME                            DEFAULT NULL,
  `enable_status`  INT(2)  NOT NULL                    DEFAULT '0',
  PRIMARY KEY (`shop_auth_id`),
  KEY `uk_shop_auth_map_shop` (`shop_id`),
  KEY `uk_shop_auth_map` (`employee_id`, `shop_id`),
  CONSTRAINT `fk_shop_auth_map_employee` FOREIGN KEY (`employee_id`) REFERENCES `tb_person_info` (`user_id`),
  CONSTRAINT `fk_shop_auth_map_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;