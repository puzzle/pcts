TRUNCATE TABLE role CASCADE;

INSERT INTO role (name, deleted_at, is_management)
VALUES
    ('Role 1', '1970-01-01 00:00:00', TRUE),
    ('Role 2', null,  FALSE);

TRUNCATE TABLE organisation_unit CASCADE;

INSERT INTO organisation_unit (name, deleted_at)
VALUES
    ('OrganisationUnit 1', '1970-01-01 00:00:00'),
    ('OrganisationUnit 2', null);

TRUNCATE TABLE certificate CASCADE;

INSERT INTO certificate (name, points, deleted_at, comment, certificate_type)
VALUES
    ('Certificate 1', 5.5, null, 'This is Certificate 1', 'CERTIFICATE'),
    ('Certificate 2', 1, null, 'This is Certificate 2', 'CERTIFICATE'),
    ('Certificate 3', 3, null, 'This is Certificate 3', 'CERTIFICATE'),
    ('Certificate 4', 0.5, null, 'This is Certificate 4', 'CERTIFICATE');

INSERT INTO certificate (name, points, deleted_at, comment, certificate_type)
VALUES
    ('LeadershipExperience 1', 5.5, null, 'This is LeadershipExperience 1', 'MILITARY_FUNCTION'),
    ('LeadershipExperience 2', 1, null, 'This is LeadershipExperience 2', 'YOUTH_AND_SPORT'),
    ('LeadershipExperience 3', 3, null, 'This is LeadershipExperience 3', 'LEADERSHIP_TRAINING');

TRUNCATE TABLE experience_type CASCADE;

INSERT INTO experience_type (name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    ('ExperienceType 1', 0, 12, 4.005),
    ('ExperienceType 2', 12, 10.7989, 6);

TRUNCATE TABLE degree_type CASCADE;

INSERT INTO degree_type (name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    ('Degree type 1', 120.55, 60, 15.45),
    ('Degree type 2', 12, 3.961, 3);

TRUNCATE TABLE tag CASCADE;

INSERT INTO tag (name)
VALUES
    ('Tag 1'),
    ('Longer tag name');

TRUNCATE TABLE CERTIFICATE_TAG CASCADE;

INSERT INTO CERTIFICATE_TAG (CERTIFICATE_ID, TAG_ID)
VALUES
    (1, 1),
    (2, 2);

TRUNCATE TABLE member CASCADE;

INSERT INTO member (first_name, last_name, abbreviation, employment_state, date_of_hire, birth_date, organisation_unit)
VALUES
    ('Member 1', 'Test', 'M1', 'MEMBER', '2021-07-15', '1999-08-10', 1),
    ('Member 2', 'Test', 'M2', 'MEMBER', '2020-06-01', '1998-03-03', 2);

INSERT INTO experience (member, name, employer, percent, experience_type, comment, start_date, end_date, deleted_at)
VALUES
    (1, 'Experience 1', 'Employer 1', 100, 1, 'Comment test 1', '2021-07-15', '2022-07-15', '1970-01-01 00:00:00'),
    (1, 'Experience 2', 'Employer 2', 80, 2, 'Comment test 2', '2022-07-16', '2023-07-15', NULL),
    (2, 'Experience 3', 'Employer 3', 60, 1, 'Comment test 3', '2023-07-16', '2024-07-15', NULL);