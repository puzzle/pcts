DROP VIEW IF EXISTS member_overview;

CREATE VIEW member_overview AS
SELECT m.id                AS member_id,
       m.first_name,
       m.last_name,
       m.abbreviation,
       m.employment_state,
       m.date_of_hire,
       m.birth_date,

       ou.name             AS organisation_unit_name,

       c.id                AS certificate_id,
       c.completed_at,
       c.valid_until,
       c.comment           AS certificate_comment,

       ct.name             AS certificate_type_name,
       ct.comment          AS certificate_type_comment,
       ct.certificate_kind AS certificate_type_kind,

       d.id                AS degree_id,
       d.name              AS degree_name,
       d.start_date        AS degree_start_date,
       d.end_date          AS degree_end_date,
       d.comment           AS degree_comment,
       dt.name             AS degree_type_name,

       e.id                AS experience_id,
       e.name              AS experience_name,
       e.employer,
       e.start_date        AS experience_start_date,
       e.end_date          AS experience_end_date,
       e.comment           AS experience_comment,
       et.name             AS experience_type_name

FROM member m

         LEFT JOIN organisation_unit ou
                   ON ou.id = m.organisation_unit

         LEFT JOIN certificate c
                   ON c.member_id = m.id
         LEFT JOIN certificate_type ct
                   ON ct.id = c.certificate_type_id

         LEFT JOIN degree d
                   ON d.member_id = m.id
         LEFT JOIN degree_type dt
                   ON dt.id = d.degree_type_id

         LEFT JOIN experience e
                   ON e.member_id = m.id
         LEFT JOIN experience_type et
                   ON et.id = e.experience_type_id;
