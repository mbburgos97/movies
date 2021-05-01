create database `movies`;

CREATE TABLE `movies`.`staff` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);

CREATE TABLE `movies`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);

ALTER TABLE `movies`.`users`
CHANGE COLUMN `userscol` `email` VARCHAR(45) NULL DEFAULT NULL ;

CREATE TABLE `movies`.`movies` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NULL,
  `thumbnail_url` VARCHAR(45) NULL,
  `trailer_url` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `movies`.`movies`
CHANGE COLUMN `thumbnail_url` `image_url` VARCHAR(45) NULL DEFAULT NULL ,
CHANGE COLUMN `trailer_url` `video_url` VARCHAR(45) NULL DEFAULT NULL ;

ALTER TABLE `movies`.`users`
DROP COLUMN `email`,
DROP COLUMN `name`;

ALTER TABLE `movies`.`users`
ADD COLUMN `created_at` TIMESTAMP NULL AFTER `password`,
ADD COLUMN `created_by` VARCHAR(45) NULL AFTER `created_at`,
ADD COLUMN `updated_by` VARCHAR(45) NULL AFTER `created_by`,
ADD COLUMN `updated_at` TIMESTAMP NULL AFTER `updated_by`;

ALTER TABLE `movies`.`users`
ADD COLUMN `memo` VARCHAR(45) NULL AFTER `updated_at`,
ADD COLUMN `login_status` INT NULL AFTER `memo`,
ADD COLUMN `status` INT NULL AFTER `login_status`;

ALTER TABLE `movies`.`movies`
ADD COLUMN `content` TEXT NULL AFTER `video_url`;

ALTER TABLE `movies`.`movies`
ADD COLUMN `created_at` TIMESTAMP NULL AFTER `content`,
ADD COLUMN `created_by` VARCHAR(45) NULL AFTER `created_at`,
ADD COLUMN `updated_at` TIMESTAMP NULL AFTER `created_by`,
ADD COLUMN `updated_by` VARCHAR(45) NULL AFTER `updated_at`,
ADD COLUMN `status` INT NULL AFTER `updated_by`,
ADD COLUMN `year` INT NULL AFTER `status`,
ADD COLUMN `director` VARCHAR(45) NULL AFTER `year`,
ADD COLUMN `awards` INT NULL AFTER `director`,
ADD COLUMN `imdb_score` DECIMAL NULL AFTER `awards`,
ADD COLUMN `investment` INT NULL AFTER `imdb_score`,
ADD COLUMN `return` INT NULL AFTER `investment`,
ADD COLUMN `return_rate` INT NULL AFTER `return`,
ADD COLUMN `payback` INT NULL AFTER `return_rate`,
ADD COLUMN `actors` VARCHAR(1000) NULL AFTER `payback`;

ALTER TABLE `movies`.`movies`
CHANGE COLUMN `return` `return_value` INT NULL DEFAULT NULL ;

CREATE TABLE `movies`.`artists` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `image_url` VARCHAR(60) NULL,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(64) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `movies`.`artists`
ADD COLUMN `created_by` VARCHAR(45) NULL AFTER `description`,
ADD COLUMN `created_at` TIMESTAMP NULL AFTER `created_by`,
ADD COLUMN `updated_by` VARCHAR(45) NULL AFTER `created_at`,
ADD COLUMN `updated_at` TIMESTAMP NULL AFTER `updated_by`;

ALTER TABLE `movies`.`movies`
ADD COLUMN `type` VARCHAR(45) NULL AFTER `actors`;

ALTER TABLE `movies`.`movies`
CHANGE COLUMN `awards` `awards` VARCHAR(100) NULL DEFAULT NULL ;

ALTER TABLE `movies`.`movies`
CHANGE COLUMN `type` `is_ongoing` TINYINT NULL DEFAULT NULL ;

ALTER TABLE `movies`.`movies`
ADD COLUMN `is_confidential` TINYINT NULL DEFAULT NULL AFTER `is_ongoing`;

ALTER TABLE `movies`.`artists`
CHANGE COLUMN `description` `description` VARCHAR(1000) NULL DEFAULT NULL ;

ALTER TABLE `movies`.`artists`
ADD COLUMN `status` INT NULL DEFAULT NULL AFTER `description`;