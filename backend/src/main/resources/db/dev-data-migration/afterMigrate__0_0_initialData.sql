TRUNCATE TABLE example CASCADE;

INSERT INTO example (id, text)
VALUES (1, 'Example 1'),
       (2, 'Example 2'),
       (3, 'Example 3'),
       (4, 'Example 4'),
       (5, 'Example 5');

SELECT setval('sequence_example', (SELECT MAX(id) FROM example));

TRUNCATE TABLE role CASCADE;

INSERT INTO role (id, name, deleted_at, is_management)
VALUES
    (1, 'Administrator', '1970-01-01 00:00:00', TRUE),
    (2, 'Manager', null, TRUE),
    (3, 'Team Lead', null, TRUE),
    (4, 'Developer', null, FALSE),
    (5, 'Intern', null, FALSE);

SELECT setval('sequence_role', (SELECT MAX(id) FROM role));
