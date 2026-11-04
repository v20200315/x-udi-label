CREATE TABLE IF NOT EXISTS `product`
(
    `id`              CHAR(36)     NOT NULL COMMENT '产品ID',

    `indicator_digit` CHAR(1)      NOT NULL COMMENT '包装指示符(Indicator Digit)，0-9',
    `company_prefix`  VARCHAR(12)  NOT NULL COMMENT 'GS1厂商识别代码(GS1 Company Prefix)',
    `item_reference`  VARCHAR(12)  NOT NULL COMMENT '商品项目代码(Item Reference)',
    `check_digit`     CHAR(1)      NOT NULL COMMENT '校验位(Check Digit)，GS1 Modulo 10计算',

    `lot_number`      VARCHAR(100)          DEFAULT NULL COMMENT '批号',
    `name`            VARCHAR(100) NOT NULL COMMENT '产品名称',
    `specification`   VARCHAR(255)          DEFAULT NULL COMMENT '产品规格',
    `model`           VARCHAR(100)          DEFAULT NULL COMMENT '产品型号',

    `production_date` DATE                  DEFAULT NULL COMMENT '生产日期',
    `expiration_date` DATE                  DEFAULT NULL COMMENT '有效期至',

    `status`          TINYINT      NOT NULL DEFAULT 1 COMMENT '状态:1-启用,0-停用',

    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`      CHAR(36)     NOT NULL COMMENT '创建人ID',
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`      CHAR(36)     NOT NULL COMMENT '更新人ID',

    PRIMARY KEY (`id`),
    KEY `idx_product_gtin14` (`indicator_digit`, `company_prefix`, `item_reference`, `check_digit`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '产品表';

CREATE TABLE IF NOT EXISTS `application_user`
(
    `id`           CHAR(36)     NOT NULL COMMENT '内部用户ID',

    `email`        VARCHAR(255) NOT NULL COMMENT '用户邮箱，同时作为用户名',
    `display_name` VARCHAR(100) NOT NULL COMMENT '用户显示名称',

    `status`       TINYINT      NOT NULL DEFAULT 1 COMMENT '状态:1-启用,0-停用',

    `created_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`   CHAR(36)     NULL COMMENT '创建人ID',
    `updated_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`   CHAR(36)     NULL COMMENT '更新人ID',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_application_user_email` (`email`),
    KEY `idx_application_user_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = '应用用户';

CREATE TABLE IF NOT EXISTS `user_identity`
(
    `id`         CHAR(36)     NOT NULL COMMENT '身份映射ID',
    `user_id`    CHAR(36)     NOT NULL COMMENT '内部用户ID',

    `issuer`     VARCHAR(255) NOT NULL COMMENT 'OIDC Issuer',
    `subject`    VARCHAR(255) NOT NULL COMMENT 'OIDC Subject',

    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by` CHAR(36)     NULL COMMENT '创建人ID',
    `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by` CHAR(36)     NULL COMMENT '更新人ID',

    PRIMARY KEY (`id`),

    UNIQUE KEY `uk_user_identity_issuer_subject` (`issuer`, `subject`),
    UNIQUE KEY `uk_user_identity_user_issuer` (`user_id`, `issuer`),

    CONSTRAINT `fk_user_identity_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `application_user` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT = '外部用户身份映射';