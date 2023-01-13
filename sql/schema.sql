/*
DROP DATABASE spring_boot;
CREATE DATABASE spring_boot;
USE spring_boot;
*/

CREATE TABLE `currency` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `symbol` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `displayname` varchar(150) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `accounts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_number` varchar(50) NOT NULL,
  `account_title` varchar(100) NOT NULL,
  `created_at` datetime NOT NULL,
  `currency_id` int DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKm507upg2gtg1crodqaw55oegd` (`account_number`),
  UNIQUE KEY `UKja08pm6u7rwinsruw7tqvl68u` (`user_id`,`account_number`),
  UNIQUE KEY `UKdmkxtf44qby3omekjegl9473k` (`account_number`,`currency_id`),
  KEY `FKks5v3om3pymdtf0wuslv1exjb` (`currency_id`),
  CONSTRAINT `FKks5v3om3pymdtf0wuslv1exjb` FOREIGN KEY (`currency_id`) REFERENCES `currency` (`id`),
  CONSTRAINT `FKnjuop33mo69pd79ctplkck40n` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `agents` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agent_number` varchar(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `account_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK2nq0fv6ph7wlvpb7vp4unnm33` (`agent_number`),
  UNIQUE KEY `UKij84shv1f52bmb9jfwl5rdl1j` (`account_id`,`user_id`),
  KEY `FK2vh8rg4inh3scgcguimya35my` (`user_id`),
  CONSTRAINT `FK2vh8rg4inh3scgcguimya35my` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9clm06rfnwdg1gfuxiktdrxd9` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `commission` double NOT NULL,
  `created_at` datetime NOT NULL,
  `description` varchar(250) NOT NULL,
  `from_transfert` int NOT NULL,
  `gain` double NOT NULL,
  `is_exit` int NOT NULL,
  `reference_number` varchar(40) NOT NULL,
  `transaction_type` varchar(40) DEFAULT NULL,
  `account_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKdt8txmnv2omxwdtymhf4v0mjk` (`reference_number`),
  KEY `accountId` (`account_id`),
  KEY `FKqwv7rmvc8va8rep7piikrojds` (`user_id`),
  CONSTRAINT `FK20w7wsg13u9srbq3bd7chfxdh` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `FKqwv7rmvc8va8rep7piikrojds` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `transfert` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `from_transaction_id` bigint NOT NULL,
  `to_transaction_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKn3otgjyfm90n976d4amw7ulnb` (`from_transaction_id`,`to_transaction_id`),
  KEY `FKofhj3sjlstm1dxyfvbrp0sxfu` (`to_transaction_id`),
  CONSTRAINT `FKofhj3sjlstm1dxyfvbrp0sxfu` FOREIGN KEY (`to_transaction_id`) REFERENCES `transactions` (`id`),
  CONSTRAINT `FKskl93kcqg26mj0jqpspnon6ay` FOREIGN KEY (`from_transaction_id`) REFERENCES `transactions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
