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

TRUNCATE TABLE experience_type CASCADE;

INSERT INTO experience_type (name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    ('Internship', 0, 12, 0),
    ('Research Project', 12, 6, 0),
    ('Volunteer Work', 8, 4, 1),
    ('Freelance Work', 0, 0, 8),
    ('Hackathon', 9, 0,0),
    ('Leadership Role', 13, 0, 0),
    ('Coursework Project', 7, 3, 1),
    ('Professional Job', 0, 8, 4);
