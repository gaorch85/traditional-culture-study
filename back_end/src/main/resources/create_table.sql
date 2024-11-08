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
                        `account` varchar(255) NOT NULL,
                        `password` varchar(255) NOT NULL,
                        `username` varchar(255) DEFAULT '小明',
                        `salt` varchar(255) NOT NULL,
                        `points` int DEFAULT '0',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `username` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE blog
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id  BIGINT       NOT NULL,
    time      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    title     VARCHAR(255) NOT NULL,
    content   TEXT         NOT NULL,
    is_public BOOLEAN               DEFAULT 0
);

CREATE TABLE comment
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    blog_id    BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    content    TEXT   NOT NULL,
    region    varchar(255)   NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE love
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    blog_id    BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE favorite
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    blog_id    BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE view
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    blog_id    BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER before_insert_answer
    BEFORE INSERT ON answer
    FOR EACH ROW
    SET NEW.date = CURDATE();

CREATE TRIGGER before_insert_theme
    BEFORE INSERT ON theme
    FOR EACH ROW
    SET NEW.date = CURDATE();

CREATE TRIGGER before_insert_question
    BEFORE INSERT ON question
    FOR EACH ROW
    SET NEW.date = CURDATE();