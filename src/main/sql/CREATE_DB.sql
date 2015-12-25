CREATE TABLE `btrack`.`users` (
    `id` SMALLINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(30) NOT NULL,
    `password` CHAR(128) NOT NULL,
    `firstname` CHAR(128) NOT NULL,
    `lastname` CHAR(128) NOT NULL,
    `is_admin` TINYINT NOT NULL,
    `creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id,username)
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`company` (
    `id` SMALLINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(30) NOT NULL,
    `creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id,name)
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`bill_freq` (
	`id` TINYINT NOT NULL AUTO_INCREMENT,
	`code` CHAR(1) NOT NULL,
	`description` CHAR(30) NOT NULL,
	`creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id,code)
) ENGINE = InnoDB;


CREATE TABLE `btrack`.`bill_master` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`company_id` SMALLINT NOT NULL,
	`freq_id` TINYINT NOT NULL,
	`amount` FLOAT NOT NULL,
	`user_id` SMALLINT NOT NULL,
	`description` VARCHAR(2048),
	`location` VARCHAR(256),
	`payment_mode` VARCHAR(512),
	`due_day` INT,
	`due_date` DATE,
	`creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id),
	FOREIGN KEY (company_id)
      REFERENCES btrack.company(id)
      ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (freq_id)
      REFERENCES btrack.bill_freq(id)
      ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (user_id)
      REFERENCES btrack.users(id)
      ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`bill` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`master_id` INT NOT NULL,
	`amount` FLOAT NOT NULL,
	`status` CHAR(2) NOT NULL,
	`creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id),
	FOREIGN KEY (master_id)
      REFERENCES bill_master(id)
      ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB;

-- Audit Tables
CREATE TABLE `btrack`.`audit_logins` (
    `user_id` SMALLINT NOT NULL,
    `login_status` INT NOT NULL,
    `login_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)
      REFERENCES users(id)
      ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`audit_services_def` (
    `id` TINYINT NOT NULL AUTO_INCREMENT,
    `name` CHAR(128) NOT NULL,
    PRIMARY KEY (id,name)
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`audit_services` (
	`service_id` TINYINT NOT NULL,
	`user_id` SMALLINT NOT NULL,
    `invocation_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)
      REFERENCES users(id)
      ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (service_id)
      REFERENCES audit_services_def(id)
      ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB;
