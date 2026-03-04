CREATE TABLE IF NOT EXISTS leadership_experience_type
(
    id                              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name                            TEXT NOT NULL UNIQUE,
    points                          NUMERIC NOT NULL,
    comment                         TEXT,
    leadership_experience_kind      TEXT NOT NULL
    ,
    deleted_at                      TIMESTAMP DEFAULT NULL
);

ALTER TABLE certificate_type
    DROP COLUMN certificate_kind CASCADE;

CREATE TABLE IF NOT EXISTS leadership_experience
(
    id                                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    member_id                           BIGINT NOT NULL,
    leadership_experience_type_id       BIGINT NOT NULL,
    comment                             TEXT,
    deleted_at                          TIMESTAMP DEFAULT NULL
);

ALTER TABLE leadership_experience
    ADD CONSTRAINT fk_leadership_experience_type
        FOREIGN KEY (leadership_experience_type_id)
            REFERENCES leadership_experience_type (id);

CREATE TABLE IF NOT EXISTS leadership_experience_calculation
(
    id                          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    calculation_id              BIGINT NOT NULL,
    leadership_experience_id    BIGINT NOT NULL
);

ALTER TABLE leadership_experience_calculation
    ADD CONSTRAINT fk_leadership_experience_calculation_calculation
        FOREIGN KEY (calculation_id)
            REFERENCES calculation (id),
    ADD CONSTRAINT fk_leadership_experience_calculation_leadership_experience
        FOREIGN KEY (leadership_experience_id)
            REFERENCES leadership_experience (id);
