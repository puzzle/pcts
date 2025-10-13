CREATE TABLE IF NOT EXISTS organisation_unit
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name            TEXT NOT NULL UNIQUE,
    deleted_at      TIMESTAMP DEFAULT NULL
);

CREATE UNIQUE INDEX organisation_unit_name_undeleted_unique
    ON organisation_unit (name)
    WHERE deleted_at IS NULL;