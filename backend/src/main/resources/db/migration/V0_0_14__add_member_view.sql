DROP VIEW IF EXISTS member_overview_view;

CREATE VIEW member_overview_view AS
WITH base_data AS (
    SELECT
        m.id AS member_id,
        m.first_name,
        m.last_name,
        m.abbreviation,
        m.birth_date,
        m.date_of_hire,
        m.employment_state,

        ou.id AS organisation_unit_id,
        ou.name AS organisation_unit_name,

        cert_row.certificate_id,
        cert_row.certificate_completed_at,
        cert_row.certificate_valid_until,
        cert_row.certificate_comment,
        cert_row.certificate_type_id,
        cert_row.certificate_type_name,
        cert_row.certificate_type_points,
        cert_row.certificate_type_comment,
        cert_row.certificate_kind,
        cert_row.tag_id,
        cert_row.tag_name,

        leader_row.leadership_id,
        leader_row.leadership_comment,
        leader_row.leadership_type_id,
        leader_row.leadership_type_name,
        leader_row.leadership_kind,
        leader_row.leadership_type_points,
        leader_row.leadership_type_comment,

        degree_row.degree_id,
        degree_row.degree_name,
        degree_row.degree_start_date,
        degree_row.degree_end_date,
        degree_row.degree_institution,
        degree_row.degree_completed,
        degree_row.degree_comment,
        degree_row.degree_type_id,
        degree_row.degree_type_name,
        degree_row.degree_highly_relevant_points,
        degree_row.degree_limited_relevant_points,
        degree_row.degree_little_relevant_points,

        exp_row.experience_id,
        exp_row.experience_name,
        exp_row.experience_employer,
        exp_row.experience_start_date,
        exp_row.experience_end_date,
        exp_row.experience_percent,
        exp_row.experience_comment,
        exp_row.experience_type_id,
        exp_row.experience_type_name,
        exp_row.experience_highly_relevant_points,
        exp_row.experience_limited_relevant_points,
        exp_row.experience_little_relevant_points,

        calc_row.calculation_id,
        calc_row.calculation_state,
        calc_row.calculation_publication_date,
        calc_row.calculation_publicized_by,
        calc_row.role_id,
        calc_row.role_name,
        calc_row.role_is_management

    FROM member m
             LEFT JOIN organisation_unit ou ON ou.id = m.organisation_unit

             LEFT JOIN LATERAL (
        SELECT
            cert.id AS certificate_id,
            cert.completed_at AS certificate_completed_at,
            cert.valid_until AS certificate_valid_until,
            cert.comment AS certificate_comment,

            ct.id AS certificate_type_id,
            ct.name AS certificate_type_name,
            ct.points AS certificate_type_points,
            ct.comment AS certificate_type_comment,
            ct.certificate_kind,

            t.id AS tag_id,
            t.name AS tag_name
        FROM certificate cert
                 JOIN certificate_type ct ON ct.id = cert.certificate_type_id
                 LEFT JOIN certificate_type_tag ctt ON ctt.certificate_type_id = ct.id
                 LEFT JOIN tag t ON t.id = ctt.tag_id
        WHERE cert.member_id = m.id
          AND cert.deleted_at IS NULL
          AND ct.certificate_kind = 'CERTIFICATE'
        ) cert_row ON TRUE

             LEFT JOIN LATERAL (
        SELECT
            leader.id AS leadership_id,
            leader.comment AS leadership_comment,

            lct.id AS leadership_type_id,
            lct.name AS leadership_type_name,
            lct.certificate_kind AS leadership_kind,
            lct.points AS leadership_type_points,
            lct.comment AS leadership_type_comment
        FROM certificate leader
                 JOIN certificate_type lct ON lct.id = leader.certificate_type_id
        WHERE leader.member_id = m.id
          AND leader.deleted_at IS NULL
          AND lct.certificate_kind != 'CERTIFICATE'
        ) leader_row ON TRUE

             LEFT JOIN LATERAL (
        SELECT
            d.id AS degree_id,
            d.name AS degree_name,
            d.start_date AS degree_start_date,
            d.end_date AS degree_end_date,
            d.institution AS degree_institution,
            d.completed AS degree_completed,
            d.comment AS degree_comment,

            dt.id AS degree_type_id,
            dt.name AS degree_type_name,
            dt.highly_relevant_points AS degree_highly_relevant_points,
            dt.limited_relevant_points AS degree_limited_relevant_points,
            dt.little_relevant_points AS degree_little_relevant_points
        FROM degree d
                 LEFT JOIN degree_type dt ON dt.id = d.degree_type_id
        WHERE d.member_id = m.id
        ) degree_row ON TRUE

             LEFT JOIN LATERAL (
        SELECT
            e.id AS experience_id,
            e.name AS experience_name,
            e.employer AS experience_employer,
            e.start_date AS experience_start_date,
            e.end_date AS experience_end_date,
            e.percent AS experience_percent,
            e.comment AS experience_comment,

            et.id AS experience_type_id,
            et.name AS experience_type_name,
            et.highly_relevant_points AS experience_highly_relevant_points,
            et.limited_relevant_points AS experience_limited_relevant_points,
            et.little_relevant_points AS experience_little_relevant_points
        FROM experience e
                 LEFT JOIN experience_type et ON et.id = e.experience_type_id
        WHERE e.member_id = m.id
        ) exp_row ON TRUE

             LEFT JOIN LATERAL (
        SELECT
            ca.id AS calculation_id,
            ca.state AS calculation_state,
            ca.publication_date AS calculation_publication_date,
            ca.publicized_by AS calculation_publicized_by,

            r.id AS role_id,
            r.name AS role_name,
            r.is_management AS role_is_management
        FROM calculation ca
                 LEFT JOIN role r ON r.id = ca.role_id
        WHERE ca.member_id = m.id
        ) calc_row ON TRUE
)
SELECT
            ROW_NUMBER() OVER (ORDER BY member_id) AS unique_row_id,
            base_data.*
FROM base_data;
