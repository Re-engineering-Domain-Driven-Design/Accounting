CREATE TABLE `accounts`
(
    `id`               BIGINT         NOT NULL PRIMARY KEY,
    `customer_id`      BIGINT         NOT NULL,
    `current_amount`   DECIMAL(12, 2) NOT NULL,
    `current_currency` VARCHAR(255)   NOT NULL
);