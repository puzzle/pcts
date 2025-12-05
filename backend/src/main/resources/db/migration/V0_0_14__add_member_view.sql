CREATE VIEW member_full_view AS
SELECT
    m.id,
    m.first_name,
    m.last_name,
    m.abbreviation,
    m.employment_state,
    m.date_of_hire,
    m.birth_date,
    m.organisation_unit

--     c.

FROM member m
         LEFT JOIN certificate c ON c.member_id = m.id
         LEFT JOIN degree d ON d.member_id = m.id
         LEFT JOIN experience e ON e.member_id = m.id
         LEFT JOIN calculation cal ON cal.member_id = m.id;