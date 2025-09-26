TRUNCATE TABLE example CASCADE;

INSERT INTO example (id, text)
VALUES (1, 'Example 1'),
       (2, 'Example 2'),
       (3, 'Example 3'),
       (4, 'Example 4'),
       (5, 'Example 5');

SELECT setval('sequence_example', (SELECT MAX(id) FROM example));

TRUNCATE TABLE role CASCADE;

INSERT INTO role (name, is_management)
VALUES
    ('Administrator', TRUE),
    ('Manager', TRUE),
    ('Team Lead', TRUE),
    ('Developer', FALSE),
    ('Intern', FALSE),
    ('Extern', FALSE),
    ('Consultant', FALSE);

TRUNCATE TABLE organisation_unit CASCADE;

INSERT INTO organisation_unit (name)
VALUES
    ('/zh'),
    ('/mobility'),
    ('/bbt'),
    ('/mid');
TRUNCATE TABLE experience_type CASCADE;

INSERT INTO experience_type (name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    ('Internship', 4, 2, 1.5),
    ('Volunteer Work', 2, 1.5, 1),
    ('Hackathon', 9, 4,2),
    ('Leadership Role', 13, 7, 5),
    ('Research Project', 6, 5, 3.25),
    ('Coursework Project', 7, 3, 1),
    ('Freelance Work', 7, 6.25, 4),
    ('Professional Job', 4.5, 2, 1);

TRUNCATE TABLE degree_type CASCADE;

INSERT INTO degree_type (name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    ('Bachelor''s Degree', 21.575, 16.5, 11),
    ('Master''s Degree', 25, 17.99, 15.325),
    ('PhD', 16, 13.555, 10.25),
    ('Associate Degree', 11, 9.5, 5.75);
