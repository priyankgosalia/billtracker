CREATE TABLE `btrack`.`users` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(30) NOT NULL,
    `password` CHAR(128) NOT NULL,
    `firstname` CHAR(128) NOT NULL,
    `lastname` CHAR(128) NOT NULL,
    `is_admin` TINYINT NOT NULL,
    `creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id,username)
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`company` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(30) NOT NULL,
    `creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id,name)
) ENGINE = InnoDB;


-- Audit Tables
CREATE TABLE `btrack`.`audit_logins` (
    `user_id` INT NOT NULL,
    `login_status` INT NOT NULL,
    `login_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)
      REFERENCES users(id)
      ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`audit_services_def` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` CHAR(128) NOT NULL,
    PRIMARY KEY (id,name)
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`audit_services` (
	`service_id` INT NOT NULL,
	`user_id` INT NOT NULL,
    `invocation_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)
      REFERENCES users(id)
      ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (service_id)
      REFERENCES audit_services_def(id)
      ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB;
