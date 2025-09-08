CREATE SEQUENCE IF NOT EXISTS sequence_role;
CREATE TABLE IF NOT EXISTS role
(
    id              BIGINT NOT NUll PRIMARY KEY,
    name            VARCHAR(250) NOT NULL,
    deleted_at      TIMESTAMP,
    is_management   BOOLEAN NOT NULL
);