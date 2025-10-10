CREATE TABLE IF NOT EXISTS member
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                TEXT,
    last_name           TEXT,
    abbreviation        TEXT,
    employment_state    TEXT,
    date_of_hire        DATE NOT NULL,
    birth_date          DATE NOT NULL,
    organisation_unit   BIGINT,
    deleted_at          TIMESTAMP DEFAULT NULL
);