CREATE TABLE `sales_settlements`
(
    `id`             BIGINT         NOT NULL PRIMARY KEY,
    `order_id`       VARCHAR(255)   NOT NULL,
    `account_id`     VARCHAR(255)   NOT NULL,
    `total_amount`   DECIMAL(12, 2) NOT NULL,
    `total_currency` VARCHAR(255)   NOT NULL
);

CREATE TABLE `sales_settlement_details`
(
    `id`                  BIGINT         NOT NULL PRIMARY KEY,
    `sales_settlement_id` BIGINT         NOT NULL,
    `amount`              DECIMAL(12, 2) NOT NULL,
    `currency`            VARCHAR(255)   NOT NULL
);
