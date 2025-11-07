CREATE TABLE IF NOT EXISTS member_certificate
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    member          BIGINT NOT NULL,
    certificate     BIGINT NOT NULL,
    completed_at    DATE NOT NULL ,
    valid_until     DATE,
    comment         TEXT,
    deleted_at      TIMESTAMP DEFAULT NULL
);