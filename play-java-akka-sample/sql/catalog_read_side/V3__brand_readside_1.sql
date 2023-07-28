DROP TABLE IF EXISTS BRAND;

CREATE TABLE IF NOT EXISTS BRAND
(
    BRAND_ID VARCHAR(255) NOT NULL,
    BRAND_NAME VARCHAR(500) NOT NULL,
    IS_ACTIVE BOOLEAN DEFAULT FALSE,
    IS_DELETED BOOLEAN DEFAULT FALSE,
    CREATED_TMST TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LAST_MODIFIED_TMST TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(BRAND_ID)
);