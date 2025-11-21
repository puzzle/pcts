CREATE TABLE IF NOT EXISTS certificate
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    member_id               BIGINT NOT NULL,
    certificate_type_id     BIGINT NOT NULL,
    completed_at            DATE NOT NULL ,
    valid_until             DATE,
    comment                 TEXT,
    deleted_at              TIMESTAMP DEFAULT NULL,

    CONSTRAINT fk_certificate_member
        FOREIGN KEY (member_id)
        REFERENCES member (id),

    CONSTRAINT fk_certificate_certificate_type
        FOREIGN KEY (certificate_type_id)
        REFERENCES certificate_type (id)
);