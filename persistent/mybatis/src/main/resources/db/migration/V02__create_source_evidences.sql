CREATE TABLE `source_evidences`
(
    `id`          BIGINT       AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `customer_id` BIGINT       NOT NULL,
    `type`        VARCHAR(255) NOT NULL
);

