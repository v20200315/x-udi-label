CREATE TABLE IF NOT EXISTS product
(
    id            CHAR(36)     NOT NULL,
    name          VARCHAR(100) NOT NULL,
    udi_di        VARCHAR(50)  NOT NULL,
    status        TINYINT      NOT NULL DEFAULT 1,

    created_at    DATETIME     NOT NULL,
    created_by    CHAR(36)     NOT NULL,
    updated_at    DATETIME     NOT NULL,
    updated_by    CHAR(36)     NOT NULL,

    PRIMARY KEY (id),
    KEY idx_product_udi_di (udi_di)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;