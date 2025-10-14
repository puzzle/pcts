CREATE TABLE IF NOT EXISTS role
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name            TEXT NOT NULL UNIQUE,
    deleted_at      TIMESTAMP DEFAULT NULL,
    is_management   BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE UNIQUE INDEX role_name_undeleted_unique
    ON role (name)
    WHERE deleted_at IS NULL;