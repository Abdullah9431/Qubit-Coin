SHOW DATABASES;
CREATE DATABASE IF NOT EXISTS `BLOCKCHAIN`;
USE `BLOCKCHAIN`;

DROP TABLE IF EXISTS `USER`;

CREATE TABLE `USER` (
    `USER_ID` INT AUTO_INCREMENT,
    `USER_NAME` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`USER_ID`)
);

INSERT INTO `USER` VALUES(NULL, 'ABDULLAH');
INSERT INTO `USER` VALUES(NULL, 'ALI');

SELECT * FROM `USER`;
