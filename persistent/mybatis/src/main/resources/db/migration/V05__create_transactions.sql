CREATE TABLE `transactions`
(
    `id`                 BIGINT         NOT NULL PRIMARY KEY,
    `account_id`         BIGINT         NOT NULL,
    `source_evidence_id` BIGINT         NOT NULL,
    `amount`             DECIMAL(12, 2) NOT NULL,
    `currency`           VARCHAR(255)   NOT NULL,
    `created_at`          TIMESTAMP      NOT NULL

)