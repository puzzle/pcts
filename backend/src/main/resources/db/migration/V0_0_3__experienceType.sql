CREATE TABLE IF NOT EXISTS experience_type
(
    id                        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name                      TEXT    NOT NULL,
    highly_relevant_points    NUMERIC,
    limited_relevant_points   NUMERIC,
    little_relevant_points    NUMERIC,
    deleted_at      TIMESTAMP DEFAULT NULL
);