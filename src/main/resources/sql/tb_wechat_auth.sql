CREATE TABLE `tb_wechat_auth` (
  `wechat_auth_id` INT(10)       NOT NULL AUTO_INCREMENT,
  `user_id`        INT(10)       NOT NULL,
  # 不能设置 VARCHAR(1000) 太长了
  `open_id`        VARCHAR(255) NOT NULL,
  `create_time`    DATETIME               DEFAULT NULL,
  PRIMARY KEY (wechat_auth_id),
  # 外键约束
  CONSTRAINT `fk_wechat_auth_profile` FOREIGN KEY (user_id) REFERENCES tb_person_info (user_id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8;

# 因为少设置了一个唯一索引，所以再添加
ALTER TABLE tb_wechat_auth
  ADD UNIQUE INDEX (open_id);