CREATE TABLE IF NOT EXISTS experience_type
(
    id                      BIGINT NOT NUll PRIMARY KEY,
    name                    TEXT    NOT NULL,
    highly_relevant_points    DECIMAL NOT NULL,
    limited_relevant_points   DECIMAL NOT NULL,
    little_relevant_points    DECIMAL NOT NULL
);