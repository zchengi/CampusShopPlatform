CREATE TABLE `tb_user_award_map` (
  `user_award_id` INT(10) NOT NULL  AUTO_INCREMENT,
  `user_id`       INT(10) NOT NULL,
  `award_id`      INT(10) NOT NULL,
  `shop_id`       INT(10) NOT NULL,
  `operator_id`   INT(10) NOT NULL,
  `create_time`   DATETIME          DEFAULT NULL,
  `used_status`   INT(2)  NOT NULL  DEFAULT '0',
  `point`         INT(10)           DEFAULT NULL,
  PRIMARY KEY (`user_award_id`),
  KEY `fk_user_award_map_profile` (`user_id`),
  KEY `fk_user_award_map_award` (`award_id`),
  KEY `fk_user_award_map_shop` (`shop_id`),
  CONSTRAINT `fk_user_award_map_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`),
  CONSTRAINT `fk_user_award_map_award` FOREIGN KEY (`award_id`) REFERENCES `tb_award` (`award_id`),
  CONSTRAINT `fk_user_award_map_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;