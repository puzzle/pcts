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

TRUNCATE TABLE certificate CASCADE;

INSERT INTO certificate (name, points, deleted_at, comment)
VALUES
    ('Certificate 1', 5.5, null, 'This is Certificate 1'),
    ('Certificate 2', 1, null, 'This is Certificate 2'),
    ('Certificate 3', 3, null, 'This is Certificate 3'),
    ('Certificate 4', 0.5, null, 'This is Certificate 4');
