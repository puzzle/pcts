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

TRUNCATE TABLE experience_type CASCADE;

INSERT INTO experience_type (name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    ('ExperienceType 1', 0, 12, 4.005),
    ('ExperienceType 2', 12, 10.7989, 6)

TRUNCATE TABLE degree_type CASCADE;

INSERT INTO degree_type (name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    ('Degree type 1', 120, 60, 15);


