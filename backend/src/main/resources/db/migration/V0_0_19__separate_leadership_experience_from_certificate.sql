-- Create separate table for leadership experience
CREATE TABLE IF NOT EXISTS leadership_experience_type
(
    id                              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name                            TEXT NOT NULL UNIQUE,
    points                          NUMERIC NOT NULL,
    comment                         TEXT,
    experience_kind                 TEXT NOT NULL,
    deleted_at                      TIMESTAMP DEFAULT NULL
);

-- Delete now obsolete column
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

-- Reconstruct member view
DROP VIEW IF EXISTS member_overview;

CREATE VIEW member_overview AS
SELECT

    m.id                  AS member_id,
    m.first_name,
    m.last_name,
    m.abbreviation,
    m.employment_state,
    m.date_of_hire,
    m.birth_date,

    ou.name               AS organisation_unit_name,

    COALESCE(c.id, 0)     AS certificate_id,
    c.completed_at        AS certificate_completed_at,
    c.valid_until         AS certificate_valid_until,
    c.comment             AS certificate_comment,

    ct.name               AS certificate_type_name,
    ct.comment            AS certificate_type_comment,

    COALESCE(d.id, 0)     AS degree_id,
    d.name                AS degree_name,
    d.start_date          AS degree_start_date,
    d.end_date            AS degree_end_date,
    d.comment             AS degree_comment,
    dt.name               AS degree_type_name,

    COALESCE(e.id, 0)     AS experience_id,
    e.name                AS experience_name,
    e.employer            AS experience_employer,
    e.start_date          AS experience_start_date,
    e.end_date            AS experience_end_date,
    e.comment             AS experience_comment,
    et.name               AS experience_type_name,

    COALESCE(le.id, 0)    AS leadership_experience_id,
    le.comment            AS leadership_experience_comment,
    let.name              AS leadership_experience_type_name,
    let.experience_kind   AS leadership_experience_kind

FROM member m
         LEFT JOIN organisation_unit ou
                   ON ou.id = m.organisation_unit

         LEFT JOIN certificate c
                   ON c.member_id = m.id
         LEFT JOIN certificate_type ct
                   ON ct.id = c.certificate_type_id

         LEFT JOIN leadership_experience le
                   ON le.member_id = m.id
         LEFT JOIN leadership_experience_type let
                   ON let.id = le.leadership_experience_type_id

         LEFT JOIN degree d
                   ON d.member_id = m.id
         LEFT JOIN degree_type dt
                   ON dt.id = d.degree_type_id

         LEFT JOIN experience e
                   ON e.member_id = m.id
         LEFT JOIN experience_type et
                   ON et.id = e.experience_type_id;