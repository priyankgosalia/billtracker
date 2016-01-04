CREATE TABLE `btrack`.`users` (
    `id` SMALLINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(30) NOT NULL,
    `password` CHAR(128) NOT NULL,
    `firstname` CHAR(128) NOT NULL,
    `lastname` CHAR(128) NOT NULL,
    `is_admin` TINYINT NOT NULL,
    `enabled` TINYINT(1) NOT NULL,
    `creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id,username)
) ENGINE = InnoDB AUTO_INCREMENT=2;

CREATE TABLE `btrack`.`company` (
    `id` SMALLINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(30) NOT NULL,
    `creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id,name)
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`bill_freq` (
	`id` TINYINT(1) NOT NULL AUTO_INCREMENT,
	`code` CHAR(1) NOT NULL,
	`description` CHAR(30) NOT NULL,
	`creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id,code)
) ENGINE = InnoDB;


CREATE TABLE `btrack`.`bill_master` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`company_id` SMALLINT NOT NULL,
	`freq_id` TINYINT(1) NOT NULL,
	`amount` DECIMAL(10,2) NOT NULL,
	`user_id` SMALLINT NOT NULL,
	`description` VARCHAR(2048),
	`location` VARCHAR(256),
	`payment_mode` VARCHAR(512),
	`due_day` INT,
	`due_date` DATE,
	`auto_recur` TINYINT(1) NOT NULL,
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
	`master_bill_id` INT NOT NULL,
	`amount` DECIMAL(10,2) NOT NULL,
	`description` VARCHAR(2048),
	`payment_mode` VARCHAR(512),
	`freq_id` TINYINT(1) NOT NULL,
	`paid` TINYINT(1) NOT NULL,
	`deleted` TINYINT(1) NOT NULL,
	`auto_generated` TINYINT(1) NOT NULL DEFAULT 0,
	`due_date` DATE NOT NULL,
	`creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id),
	FOREIGN KEY (master_bill_id)
      REFERENCES bill_master(id)
      ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (freq_id)
      REFERENCES btrack.bill_freq(id)
      ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`reminder_master` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`master_bill_id` INT NOT NULL,
	`before_days` SMALLINT NOT NULL DEFAULT 0,
	`creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id),
	FOREIGN KEY (master_bill_id)
      REFERENCES bill_master(id)
      ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`reminder` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`master_reminder_id` INT NOT NULL,
	`bill_id` INT NOT NULL,
	`before_days` SMALLINT NOT NULL DEFAULT 0,
	`show` TINYINT(1) NOT NULL DEFAULT 1,
	`creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id),
	FOREIGN KEY (bill_id)
      REFERENCES bill(id)
      ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (master_reminder_id)
      REFERENCES reminder_master(id)
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
