TRUNCATE TABLE example CASCADE;


INSERT INTO example (id, text)
VALUES (1, 'Example 1'),
       (11, 'Example 2'),
       (21, 'Example 3'),
       (31, 'Example 4'),
       (41, 'Example 5');

SELECT setval('sequence_example', (SELECT MAX(id) FROM example));



