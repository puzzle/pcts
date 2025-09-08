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
    (nextval('sequence_role'), 'Administrator', '1970-01-01 00:00:00', TRUE),
    (nextval('sequence_role'), 'Manager', null, TRUE),
    (nextval('sequence_role'), 'Team Lead', null, TRUE),
    (nextval('sequence_role'), 'Developer', null, FALSE),
    (nextval('sequence_role'), 'Intern', null, FALSE);