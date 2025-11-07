CREATE TABLE IF NOT EXISTS experience
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    member_id          BIGINT NOT NULL,
    name            TEXT NOT NULL,
    employer        TEXT,
    percent         INTEGER NOT NULL,
    experience_type BIGINT NOT NULL,
    comment         TEXT,
    start_date      DATE NOT NULL,
    end_date        DATE,
    deleted_at      TIMESTAMP DEFAULT NULL
);