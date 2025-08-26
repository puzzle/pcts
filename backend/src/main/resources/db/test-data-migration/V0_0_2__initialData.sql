TRUNCATE TABLE example CASCADE;


INSERT INTO example (id, text)
VALUES (1, 'Example 1'),
       (2, 'Example 2');

SELECT setval('sequence_example', (SELECT MAX(id) FROM example));


