-- Migration 2: Migrate existing data to new tables

INSERT INTO leadership_experience_type(name, points, comment, experience_kind)
SELECT
    name,
    points,
    comment,
    certificate_kind
FROM certificate_type
WHERE certificate_kind != 'CERTIFICATE';

INSERT INTO leadership_experience(member_id, leadership_experience_type_id, comment, legacy_certificate_id)
SELECT
    c.member_id,
    let.id,
    c.comment,
    c.id
FROM certificate c
         JOIN certificate_type ct
              ON c.certificate_type_id = ct.id
         JOIN leadership_experience_type let
              ON let.name = ct.name
WHERE ct.certificate_kind != 'CERTIFICATE';

INSERT INTO leadership_experience_calculation(calculation_id, leadership_experience_id)
SELECT
    cc.calculation_id,
    le.id
FROM certificate_calculation cc
         JOIN leadership_experience le
              ON le.legacy_certificate_id = cc.certificate_id;
