CREATE TABLE IF NOT EXISTS certificate
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    member_id               BIGINT NOT NULL,
    certificate_type_id     BIGINT NOT NULL,
    completed_at            DATE NOT NULL,
    valid_until             DATE,
    comment                 TEXT,
    deleted_at              TIMESTAMP DEFAULT NULL
);