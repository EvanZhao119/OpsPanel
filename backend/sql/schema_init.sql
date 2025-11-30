-- ==========================================================
--  Database initialization script for OpsPanel System Module
--  Compatible with MySQL 8.x
-- ==========================================================
CREATE DATABASE IF NOT EXISTS opspanel
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

DROP TABLE IF EXISTS `sys_user`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_dept`;
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `sys_user_role`;
DROP TABLE IF EXISTS `sys_role_menu`;
DROP TABLE IF EXISTS `sys_login_log`;
DROP TABLE IF EXISTS `sys_oper_log`;


-- ==========================================================
--  Department Table
-- ==========================================================
CREATE TABLE `sys_dept` (
  `dept_id`     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Department ID',
  `dept_name`   VARCHAR(64)  NOT NULL COMMENT 'Department name',
  `parent_id`   BIGINT       DEFAULT NULL COMMENT 'Parent department ID',
  `order_num`   INT          DEFAULT 0 COMMENT 'Display order',
  `status`      TINYINT      DEFAULT 1 COMMENT '1 = enabled, 0 = disabled',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
  `deleted`     TINYINT      DEFAULT 0 COMMENT 'Logical deletion flag (0=normal,1=deleted)',
  INDEX `idx_parent`(`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Department';


-- ==========================================================
--  Role Table
-- ==========================================================
CREATE TABLE `sys_role` (
  `role_id`     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Role ID',
  `role_name`   VARCHAR(64)  NOT NULL COMMENT 'Role name',
  `role_key`    VARCHAR(64)  NOT NULL COMMENT 'Role key (permission identifier)',
  `role_sort`   INT          DEFAULT 1 COMMENT 'Display order',
  `status`      TINYINT      DEFAULT 1 COMMENT '1 = enabled, 0 = disabled',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
  `deleted`     TINYINT      DEFAULT 0 COMMENT 'Logical deletion flag',
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Role';


-- ==========================================================
--  Menu Table
-- ==========================================================
CREATE TABLE `sys_menu` (
  `menu_id`     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Menu ID',
  `menu_name`   VARCHAR(64)  NOT NULL COMMENT 'Menu name',
  `parent_id`   BIGINT       DEFAULT NULL COMMENT 'Parent menu ID',
  `path`        VARCHAR(128) DEFAULT NULL COMMENT 'Frontend route path',
  `component`   VARCHAR(128) DEFAULT NULL COMMENT 'Frontend component path',
  `icon`        VARCHAR(64)  DEFAULT NULL COMMENT 'Menu icon',
  `order_num`   INT          DEFAULT 0 COMMENT 'Display order',
  `visible`     TINYINT      DEFAULT 0 COMMENT 'Visibility flag (0=visible,1=hidden)',
  `status`      TINYINT      DEFAULT 1 COMMENT '1 = enabled, 0 = disabled',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
  `deleted`     TINYINT      DEFAULT 0 COMMENT 'Logical deletion flag',
  INDEX `idx_parent`(`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Menu';


-- ==========================================================
--  User Table
-- ==========================================================
CREATE TABLE `sys_user` (
  `user_id`     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'User ID',
  `username`    VARCHAR(64)  NOT NULL COMMENT 'Username (login)',
  `password`    VARCHAR(128) NOT NULL COMMENT 'Password (hashed)',
  `nick_name`   VARCHAR(64)  DEFAULT NULL COMMENT 'Nickname',
  `email`       VARCHAR(128) DEFAULT NULL COMMENT 'Email address',
  `phone`       VARCHAR(32)  DEFAULT NULL COMMENT 'Phone number',
  `dept_id`     BIGINT       DEFAULT NULL COMMENT 'Department ID',
  `status`      TINYINT      DEFAULT 1 COMMENT '1 = enabled, 0 = disabled',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
  `deleted`     TINYINT      DEFAULT 0 COMMENT 'Logical deletion flag',
  UNIQUE KEY `uk_username` (`username`),
  INDEX `idx_dept`(`dept_id`),
  CONSTRAINT `fk_user_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept`(`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System User';


-- ==========================================================
--  User-Role Mapping
-- ==========================================================
CREATE TABLE `sys_user_role` (
  `user_id` BIGINT NOT NULL COMMENT 'User ID',
  `role_id` BIGINT NOT NULL COMMENT 'Role ID',
  PRIMARY KEY (`user_id`, `role_id`),
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role`(`role_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Mapping between Users and Roles';


-- ==========================================================
--  Role-Menu Mapping
-- ==========================================================
CREATE TABLE `sys_role_menu` (
  `role_id` BIGINT NOT NULL COMMENT 'Role ID',
  `menu_id` BIGINT NOT NULL COMMENT 'Menu ID',
  PRIMARY KEY (`role_id`, `menu_id`),
  CONSTRAINT `fk_role_menu_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role`(`role_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_role_menu_menu` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu`(`menu_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Mapping between Roles and Menus';


-- ==========================================================
--  Login Log Table
-- ==========================================================
CREATE TABLE `sys_login_log` (
  `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Log ID',
  `username`    VARCHAR(64)  NOT NULL COMMENT 'Login username',
  `ipaddr`      VARCHAR(64)  DEFAULT NULL COMMENT 'Login IP',
  `login_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT 'Login timestamp',
  `status`      TINYINT      DEFAULT 1 COMMENT 'Login status (1=success,0=failure)',
  `msg`         VARCHAR(255) DEFAULT NULL COMMENT 'Message / reason'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Login Logs';


-- ==========================================================
--  Operation Log Table
-- ==========================================================
CREATE TABLE `sys_oper_log` (
  `oper_id`     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Operation ID',
  `title`       VARCHAR(255) DEFAULT NULL COMMENT 'Module title',
  `method`      VARCHAR(255) DEFAULT NULL COMMENT 'Method name',
  `request_uri` VARCHAR(255) DEFAULT NULL COMMENT 'Request URI',
  `request_method` VARCHAR(10) DEFAULT NULL COMMENT 'HTTP method',
  `operator`    VARCHAR(64)  DEFAULT NULL COMMENT 'Operator name',
  `oper_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT 'Operation time',
  `status`      TINYINT      DEFAULT 1 COMMENT 'Status (1=success,0=failure)',
  `error_msg`   TEXT COMMENT 'Error message if failed'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Operation Logs';

-- ==========================================================
--  Operation Tasks Table
-- ==========================================================
CREATE TABLE `task_job` (
  `id`              bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key of the scheduled job',
  `job_name`        varchar(100)  NOT NULL COMMENT 'Human-readable name of the job',
  `job_group`       varchar(50)   NOT NULL DEFAULT 'DEFAULT' COMMENT 'Logical group of the job',
  `job_type`        varchar(32)   NOT NULL DEFAULT 'JAVA' COMMENT 'Type of job: JAVA / HTTP / AGENT etc.',
  `invoke_target`   varchar(500)  NOT NULL COMMENT 'Target to invoke (bean.method / URL / agent key)',
  `cron_expression` varchar(255)  NOT NULL COMMENT 'Cron expression defining the schedule',
  `misfire_policy`  char(1)       NOT NULL DEFAULT '1' COMMENT 'Misfire policy: 1=default 2=fire now 3=ignore',
  `concurrent`      char(1)       NOT NULL DEFAULT '1' COMMENT 'Whether concurrent executions are allowed: 0=no 1=yes',
  `status`          char(1)       NOT NULL DEFAULT '0' COMMENT 'Job status: 0=active 1=paused',
  `remark`          varchar(255)  DEFAULT NULL COMMENT 'Optional remark',
  `create_by`       varchar(64)   DEFAULT NULL COMMENT 'Created by',
  `create_time`     datetime      DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
  `update_by`       varchar(64)   DEFAULT NULL COMMENT 'Last updated by',
  `update_time`     datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Scheduled job table';

CREATE TABLE `task_log` (
  `id`              bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key of the log entry',
  `job_id`          bigint NOT NULL COMMENT 'Reference to the scheduled job',
  `job_name`        varchar(100) NOT NULL COMMENT 'Job name at the time of execution',
  `job_group`       varchar(50)  NOT NULL COMMENT 'Job group at the time of execution',
  `job_type`        varchar(32)  NOT NULL COMMENT 'Job type at the time of execution',
  `invoke_target`   varchar(500) NOT NULL COMMENT 'Target invoked during execution',
  `status`          char(1)      NOT NULL DEFAULT '0' COMMENT 'Execution result: 0=success 1=failure',
  `start_time`      datetime     DEFAULT NULL COMMENT 'Execution start time',
  `end_time`        datetime     DEFAULT NULL COMMENT 'Execution end time',
  `elapsed_ms`      bigint       DEFAULT NULL COMMENT 'Execution time in milliseconds',
  `job_message`     varchar(500) DEFAULT NULL COMMENT 'Short execution message',
  `exception_info`  text         DEFAULT NULL COMMENT 'Exception stack trace if any',
  `create_time`     datetime     DEFAULT CURRENT_TIMESTAMP COMMENT 'Log creation time',
  PRIMARY KEY (`id`),
  KEY `idx_job_id` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Scheduled job execution log table';
