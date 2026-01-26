INSERT INTO role(name, is_management)
VALUES ('T2 Software Engineer', FALSE);

INSERT INTO member(first_name, last_name, abbreviation, employment_state, date_of_hire, birth_date, organisation_unit)
VALUES ('John', 'Doe', 'JD', 'MEMBER', '2020-01-01', '1990-01-01', 1);

INSERT INTO certificate_type(name, points, certificate_kind)
VALUES ('Certificate', 0.5, 'CERTIFICATE');

INSERT INTO certificate(member_id, certificate_type_id, completed_at, valid_until)
VALUES (1000, 1000, '2020-01-05', null);

INSERT INTO experience_type(name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES ('Experience_type', 3, 0, 0);

INSERT INTO experience(member_id, name, employer, percent, experience_type_id, start_date, end_date)
VALUES (1000, 'Experience', 'Whoever', 100, 1000, '2021-08-01', '2022-07-31'),

INSERT INTO degree_type(name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES ('Apprenticeship', 5, 4, 3),

INSERT INTO degree(member_id, name, institution, completed, degree_type_id, start_date, end_date)
VALUES (1000, 'Lehre (real)', 'BIT', true, 1000, '2020-02-03', null),

INSERT INTO calculation(member_id, role_id, state, publication_date, publicized_by)
VALUES (1000, 1000, 'ACTIVE', '2020-01-01', 'root');

INSERT INTO certificate_calculation(calculation_id, certificate_id)
VALUES (1000, 1000);

INSERT INTO experience_calculation(calculation_id, experience_id, relevancy)
VALUES (1000, 1000, 'STRONGLY'),

INSERT INTO degree_calculation(calculation_id, degree_id, weight, relevancy)
VALUES (1000, 1000, 100, 'STRONGLY'),
