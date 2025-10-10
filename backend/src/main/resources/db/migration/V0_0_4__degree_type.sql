CREATE TABLE IF NOT EXISTS degree_type
(
    id                        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                      TEXT    NOT NULL,
    deleted_at                TIMESTAMP DEFAULT NULL,
    highly_relevant_points    NUMERIC,
    limited_relevant_points   NUMERIC,
    little_relevant_points    NUMERIC
);