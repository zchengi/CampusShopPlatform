CREATE TABLE `tb_area` (
  `area_id`        INT(2)       NOT NULL  AUTO_INCREMENT,
  `area_name`      VARCHAR(200) NOT NULL,
  `priority`       INT(2)       NOT NULL  DEFAULT 0,
  `create_time`    DATETIME               DEFAULT NULL,
  `last_edit_time` DATETIME               DEFAULT NULL,
  # 主键
  PRIMARY KEY (area_id),
  # 唯一键
  UNIQUE KEY `uk_area`(area_name)
)
  # 引擎
  ENGINE = InnoDB
  # 自动递增
  AUTO_INCREMENT = 1
  # 字符集
  DEFAULT CHARSET = utf8;
