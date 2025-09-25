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

INSERT INTO certificate (name, points, deleted_at, comment, certificate_type)
VALUES
    ('Certificate 1', 5.5, null, 'This is Certificate 1', 'CERTIFICATE'),
    ('Certificate 2', 1, null, 'This is Certificate 2', 'CERTIFICATE'),
    ('Certificate 3', 3, null, 'This is Certificate 3', 'CERTIFICATE'),
    ('Certificate 4', 0.5, null, 'This is Certificate 4', 'CERTIFICATE'),
    ('LeadershipExperience 1', 5.5, null, 'This is LeadershipExperience 1', 'MILITARY_FUNCTION'),
    ('LeadershipExperience 2', 1, null, 'This is LeadershipExperience 2', 'YOUTH_AND_SPORT'),
    ('LeadershipExperience 3', 3, null, 'This is LeadershipExperience 3', 'LEADERSHIP_TRAINING');
