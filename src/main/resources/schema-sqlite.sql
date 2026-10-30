CREATE TABLE IF NOT EXISTS product
(
    id              TEXT    NOT NULL PRIMARY KEY,

    indicator_digit TEXT    NOT NULL,
    company_prefix  TEXT    NOT NULL,
    item_reference  TEXT    NOT NULL,
    check_digit     TEXT    NOT NULL,

    lot_number      TEXT,
    name            TEXT    NOT NULL,
    specification   TEXT,
    model           TEXT,

    production_date TEXT,
    expiration_date TEXT,

    status          INTEGER NOT NULL DEFAULT 1,

    created_at      TEXT    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      TEXT    NOT NULL,

    updated_at      TEXT    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      TEXT    NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_product_gtin14
    ON product (
    indicator_digit,
    company_prefix,
    item_reference,
    check_digit
    );