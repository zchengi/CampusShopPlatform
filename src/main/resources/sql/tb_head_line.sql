CREATE TABLE `tb_head_line` (
  `line_id`        INT(100)      NOT NULL AUTO_INCREMENT,
  `line_name`      VARCHAR(1000)          DEFAULT NULL,
  `line_link`      VARCHAR(2000) NOT NULL,
  `line_img`       VARCHAR(2000) NOT NULL,
  `priority`       INT(2)                 DEFAULT NULL,
  `enable_status`  INT(2)        NOT NULL DEFAULT 0,
  `create_time`    DATETIME               DEFAULT NULL,
  `last_edit_time` DATETIME               DEFAULT NULL,
  PRIMARY KEY (line_id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;