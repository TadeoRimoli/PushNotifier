CREATE TABLE `account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `accountname` varchar(50) NOT NULL,
  `password` varchar(250) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `message` varchar(300) DEFAULT NULL,
  `expotoken` varchar(250) DEFAULT NULL,
  `datetime` datetime NOT NULL,
  `multipleusers` tinyint DEFAULT NULL,
  `user` bigint DEFAULT NULL,
  `event` bigint DEFAULT NULL,
  `account` bigint DEFAULT NULL,
  `entity` bigint DEFAULT NULL,
  `gettokensurl` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_notification_account_idx` (`account`),
  CONSTRAINT `fk_notification_account` FOREIGN KEY (`account`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `refreshtoken` (
  `refreshtoken` int NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `account` bigint NOT NULL,
  `expiration` datetime NOT NULL,
  PRIMARY KEY (`refreshtoken`),
  KEY `fk_refreshtoken_account_idx` (`account`),
  CONSTRAINT `fk_refreshtoken_account` FOREIGN KEY (`account`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;