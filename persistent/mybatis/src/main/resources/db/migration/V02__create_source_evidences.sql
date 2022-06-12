CREATE TABLE `source_evidences`
(
    `id`          BIGINT       NOT NULL PRIMARY KEY,
    `customer_id` BIGINT       NOT NULL,
    `type`        VARCHAR(255) NOT NULL
);

