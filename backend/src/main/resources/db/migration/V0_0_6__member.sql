CREATE TABLE IF NOT EXISTS member
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    first_name          TEXT NOT NULL,
    last_name           TEXT NOT NULL,
    abbreviation        TEXT NOT NULL,
    employment_state    TEXT NOT NULL,
    date_of_hire        DATE,
    birth_date          DATE NOT NULL,
    organisation_unit   BIGINT,
    deleted_at          TIMESTAMP DEFAULT NULL
);