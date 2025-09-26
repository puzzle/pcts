TRUNCATE TABLE example CASCADE;

INSERT INTO example (id, text)
VALUES (1, 'Example 1'),
       (2, 'Example 2');

SELECT setval('sequence_example', (SELECT MAX(id) FROM example));

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

INSERT INTO certificate (name, points, deleted_at, comment)
VALUES
    ('Certificate 1', 5.5, null, 'This is Certificate 1'),
    ('Certificate 2', 1, null, 'This is Certificate 2'),
    ('Certificate 3', 3, null, 'This is Certificate 3'),
    ('Certificate 4', 0.5, null, 'This is Certificate 4');

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
