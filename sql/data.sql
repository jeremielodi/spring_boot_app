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

-- mvn install
-- mvn spring-boot:run
-- http://localhost:8080/api/auth/signup

/* ADMIN
{
    "username" : "Jeremie LODI",
    "email": "jeremielodi@gmail.com",
    "role": ["admin"],
    "password":"123456"
}

/ other users
{
    "username" : "Mayele",
    "email": "mayele@gmail.com",
    "password":"tester"
}


POST http://localhost:8080/api/auth/signin
{
    "username" : "Mayele",
    "password":"tester"
}

response:
{
    "id": 2,
    "username": "Mayele",
    "email": "mayele@gmail.com",
    "roles": [
        "ROLE_USER"
    ],
    "accessToken": "....long_string_token...",
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNYXllbGUiLCJpYXQiOjE2NzM1MTk1NjYsImV4cCI6MTY3MzYwNTk2Nn0.XimzfhHCNBIXNPliK6pW8VOi5UoooWobV37Hxp1Pih7YQXRfuMfwcmCl50l4eYccs4GBDV32Q2vNY-mbooJf_g",
    "tokenType": "Bearer"
}


POST http://localhost:8080/api/transactions/withdrawal

{
    "accountNumber" : "12002132",
    "amount" : 25,
    "agentNumber": "2300678",
    "description" : "retrait chez un agent"
}

POST http://localhost:8080/api/transactions/transfert

{
    "fromAccountNumber" : "12002132",
    "toAccountNumber" : "12002133",
    "amount" : 10,
    "description" : "Transfert argent"
}

*/