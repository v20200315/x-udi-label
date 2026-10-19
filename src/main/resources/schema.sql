CREATE TABLE IF NOT EXISTS `product`
(
    `id`              CHAR(36)     NOT NULL COMMENT '商品ID',
    `udi_di`          VARCHAR(100) NOT NULL COMMENT 'UDI DI(器械标识)',
    `lot_number`      VARCHAR(100)          DEFAULT NULL COMMENT '批号',
    `name`            VARCHAR(100) NOT NULL COMMENT '商品名称',
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
    KEY `idx_product_udi_di` (`udi_di`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '商品表';


CREATE TABLE IF NOT EXISTS `label`
(
    `id`              CHAR(36)     NOT NULL COMMENT '标签ID',
    `product_id`      CHAR(36)     NOT NULL COMMENT '关联商品ID',

    `name`            VARCHAR(100) NOT NULL COMMENT '标签名称',

    `product_name`    VARCHAR(100) NOT NULL COMMENT '商品名称快照',
    `udi_di`          VARCHAR(100) NOT NULL COMMENT 'UDI DI快照',

    `code_type`       VARCHAR(32)  NOT NULL COMMENT '编码类型',
    `code_content`    TEXT         NOT NULL COMMENT '实际编码内容',

    `image_url`       VARCHAR(512)          DEFAULT NULL COMMENT '标签图片地址',

    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by`      CHAR(36)     NOT NULL COMMENT '创建人ID',
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updated_by`      CHAR(36)     NOT NULL COMMENT '更新人ID',

    PRIMARY KEY (`id`),
    KEY `idx_label_product_id` (`product_id`),
    KEY `idx_label_created_at` (`created_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '产品标签表';