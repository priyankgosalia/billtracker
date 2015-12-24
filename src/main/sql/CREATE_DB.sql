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
    `user_id` INT NOT NULL AUTO_INCREMENT,
    `success` INT NOT NULL,
    `failure` INT NOT NULL,
    FOREIGN KEY (user_id)
      REFERENCES users(id)
      ON UPDATE CASCADE ON DELETE CASCADE,
) ENGINE = InnoDB;

CREATE TABLE `btrack`.`audit_services` (
    `service_name` CHAR(128) NOT NULL,
    `count` INT NOT NULL,
    PRIMARY KEY (id,name)
) ENGINE = InnoDB;
