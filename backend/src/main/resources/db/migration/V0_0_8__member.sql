CREATE TABLE IF NOT EXISTS member
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                TEXT NOT NULL,
    last_name           TEXT NOT NULL,
    abbreviation        TEXT,
    employment_state    TEXT NOT NULL,
    date_of_hire        DATE NOT NULL,
    birth_date          DATE,
    organisation_unit   BIGINT,
    deleted_at          TIMESTAMP DEFAULT NULL
);