CREATE OR REPLACE FUNCTION build_member_overview(p_member_id BIGINT)
    RETURNS JSONB
    LANGUAGE sql
    STABLE
AS $$
WITH base AS (
    SELECT *
    FROM member m
    WHERE m.id = p_member_id
),
     cert AS (
         SELECT jsonb_agg(
                        jsonb_build_object(
                                'id', c.id,
                                'certificate', jsonb_build_object(
                                        'id', ct.id,
                                        'name', ct.name,
                                        'points', ct.points,
                                        'comment', ct.comment
                                               ),
                                'completion', c.completed_at,
                                'validUntil', c.valid_until,
                                'comment', c.comment
                        )
                        ORDER BY c.completed_at
                ) AS certificates
         FROM certificate c
                  LEFT JOIN certificate_type ct
                            ON ct.id = c.certificate_type_id
         WHERE c.member_id = p_member_id
           AND c.deleted_at IS NULL
     ),
     degrees AS (
         SELECT jsonb_agg(
                        jsonb_build_object(
                                'id', d.id,
                                'name', d.name,
                                'startDate', d.start_date,
                                'endDate', d.end_date,
                                'institution', d.institution,
                                'completed', d.completed,
                                'type', jsonb_build_object(
                                        'degree_type_id', dt.id,
                                        'name', dt.name,
                                        'highlyRelevantPoints', dt.highly_relevant_points,
                                        'limitedRelevantPoints', dt.limited_relevant_points,
                                        'littleRelevantPoints', dt.little_relevant_points
                                        )
                        )
                        ORDER BY d.start_date
                ) AS degrees
         FROM degree d
                  LEFT JOIN degree_type dt ON dt.id = d.degree_type_id
         WHERE d.member_id = p_member_id
     ),
     experience AS (
         SELECT jsonb_agg(
                        jsonb_build_object(
                                'id', e.id,
                                'name', e.name,
                                'employer', e.employer,
                                'startDate', e.start_date,
                                'endDate', e.end_date,
                                'percent', e.percent,
                                'comment', e.comment,
                                'type', jsonb_build_object(
                                        'id', et.id,
                                        'name', et.name,
                                        'highlyRelevantPoints', et.highly_relevant_points,
                                        'limitedRelevantPoints', et.limited_relevant_points,
                                        'littleRelevantPoints', et.little_relevant_points
                                        )
                        )
                        ORDER BY e.start_date
                ) AS experiences
         FROM experience e
                  LEFT JOIN experience_type et ON et.id = e.experience_type_id
         WHERE e.member_id = p_member_id
     )
SELECT jsonb_build_object(
               'member', jsonb_build_object(
                'id', m.id,
                'firstName', m.first_name,
                'lastName', m.last_name,
                'abbreviation', m.abbreviation,
                'dateOfBirth', m.birth_date,
                'dateOfHire', m.date_of_hire,
                'employmentState', m.employment_state,
                'organisationUnit', jsonb_build_object(
                        'id', ou.id,
                        'name', ou.name
                                    )
                         ),
               'cv', jsonb_build_object(
                       'degrees', COALESCE(d.degrees, '[]'::jsonb),
                       'certificates', COALESCE(c.certificates, '[]'::jsonb),
                       'experiences', COALESCE(x.experiences, '[]'::jsonb),
                       'leadershipExperiences', '[]'::jsonb   -- You will fill these in
                     ),
               'calculations', '[]'::jsonb               -- You will fill this in later
       )
FROM base m
         LEFT JOIN organisation_unit ou ON ou.id = m.organisation_unit
         LEFT JOIN cert c ON TRUE
         LEFT JOIN degrees d ON TRUE
         LEFT JOIN experience x ON TRUE;
$$;

CREATE OR REPLACE VIEW member_overview_view AS
SELECT
    m.id AS member_id,
    build_member_overview(m.id) AS overview
FROM member m;
