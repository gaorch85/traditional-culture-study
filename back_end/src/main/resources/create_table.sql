CREATE TABLE `answer` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `userId` bigint NOT NULL,
                          `content` varchar(1023) NOT NULL,
                          `score` int NOT NULL,
                          `reason` varchar(1023) NOT NULL,
                          `questionId` bigint NOT NULL,
                          `date` date DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `message` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `conversationId` bigint NOT NULL,
                           `isPerson` tinyint(1) NOT NULL,
                           `content` text NOT NULL,
                           `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=851 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `question` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `level` int NOT NULL,
                            `content` varchar(1023) NOT NULL,
                            `date` date DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `theme` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `content` varchar(255) NOT NULL,
                         `date` date DEFAULT NULL,
                         `intro` varchar(1023) NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `username` varchar(255) NOT NULL,
                        `password` varchar(255) NOT NULL,
                        `nickname` varchar(255) DEFAULT '小明',
                        `salt` varchar(255) NOT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci