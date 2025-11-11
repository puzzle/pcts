CREATE TABLE IF NOT EXISTS certificate_type
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name            TEXT NOT NULL UNIQUE,
    points          NUMERIC NOT NULL,
    deleted_at      TIMESTAMP DEFAULT NULL,
    comment         TEXT,
    certificate_kind    TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS tag (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name TEXT NOT NULL
);

CREATE UNIQUE INDEX tag_name_lower ON tag (LOWER(name));

CREATE TABLE certificate_type_tag
(
    certificate_type_id BIGINT NOT NULL,
    tag_id         BIGINT NOT NULL,
    PRIMARY KEY (certificate_type_id, tag_id),
    CONSTRAINT fk_certificate FOREIGN KEY (certificate_type_id)
        REFERENCES certificate_type (id),
    CONSTRAINT fk_tag FOREIGN KEY (tag_id)
        REFERENCES tag (id)
        ON DELETE CASCADE
);
