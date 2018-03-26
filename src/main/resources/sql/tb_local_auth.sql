CREATE TABLE `tb_local_auth` (
  `local_auth_id`  INT(10)      NOT NULL  AUTO_INCREMENT,
  `user_id`        INT(10)      NOT NULL,
  `username`       VARCHAR(128) NOT NULL,
  `password`       VARCHAR(128) NOT NULL,
  `create_time`    DATETIME               DEFAULT NULL,
  `last_edit_time` DATETIME               DEFAULT NULL,
  PRIMARY KEY (local_auth_id),
  UNIQUE KEY `uk_local_profile` (username),
  # 外键约束
  CONSTRAINT `fk_local_auth_profile` FOREIGN KEY (user_id) REFERENCES tb_person_info (user_id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;