-- CREATE DATABASE spring_boot;
-- USE spring_boot;

INSERT INTO `currency`(`id`, `name`,`symbol`) VALUES
(1, 'FRANC CEFA', 'FCFA'),
(2, 'UNITED STATE DOLLAR', 'USD');

INSERT INTO roles(NAME) VALUE('ROLE_ADMIN');
INSERT INTO roles(NAME) VALUE('ROLE_USER');


INSERT INTO `users` (`id`,`displayname`, `email`, `username`, `password`) VALUES 
(1, 'Jeremie LODI', 'jeremielodi@gmail.com', 'jeremielodi@gmail.com', '$2a$10$YfyHW9aaIV9o/L8Hz8Keour6/aKI.MTL52ScXVo0q/.YPpVOGUNn6'),
(2, 'Mayele', 'mayele@gmail.com', 'mayele@gmail.com', '$2a$10$lxliH24uH4mb3pUUSkHu8OR.B00Noepat6XUsBKHx2WVrcFkU/PXq'),
(3, 'John Doe', 'agent@digipay.org', 'agent@digipay.org', '$2a$10$YfyHW9aaIV9o/L8Hz8Keour6/aKI.MTL52ScXVo0q/.YPpVOGUNn6');


INSERT INTO `user_roles`(`user_id`,`role_id`) VALUE 
(1, 1),
(2, 2),
(3, 2);


INSERT INTO `accounts`(`id`, `account_number`, `account_title`, `currency_id`, `user_id`, `created_at`) VALUES
(1, '12002133', 'LODI OMATOKO JEREMIE', 2, 1, NOW()), /* USD account*/
(2, '12002132', 'MAYELE MULALA JACK', 2, 2, NOW()), /* USD account*/
(3, '12002122', 'JOHN DOE', 2, 3, NOW()), /* USD account*/
(4, '12602132', 'JOHN DOE', 1, 3, NOW()); /* CFA account*/


INSERT INTO `agents`(`id`, `agent_number`, `name`, `account_id`, `user_id`)VALUES
(1, '2300678', 'JOHN DOE', 3, 3),
(2, '4300158', 'JOHN DOE', 4, 3);

INSERT INTO `transactions`(`amount`, `gain`, `commission`, `account_id`, `is_exit`,  `reference_number`, `user_id`, `transaction_type`, `description`, `from_transfert`, `created_at`) VALUES
(200, 0.2, 0.1, 2, 0, '1000000001', 1,  'DEPOSIT', 'Depot creation compte', 0, '2023-01-01 07:30:00');
