TRUNCATE TABLE example CASCADE;

INSERT INTO example (id, text)
VALUES (1, 'Example 1'),
       (2, 'Example 2');

SELECT setval('sequence_example', (SELECT MAX(id) FROM example));

TRUNCATE TABLE role CASCADE;

INSERT INTO role (id, name, deleted_at, is_management)
VALUES
    (1, 'Role 1', '1970-01-01 00:00:00', TRUE),
    (2, 'Role 2', null, false);

SELECT setval('sequence_role', (SELECT MAX(id) FROM role));


