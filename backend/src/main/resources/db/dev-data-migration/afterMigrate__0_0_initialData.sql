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
