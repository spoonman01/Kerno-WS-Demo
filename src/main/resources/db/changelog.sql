-- liquibase formatted sql

-- changeset lucarospocher:1.0.0
CREATE TABLE message
(
    id SERIAL PRIMARY KEY,
    text VARCHAR(1024) NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- rollback DROP TABLE message;