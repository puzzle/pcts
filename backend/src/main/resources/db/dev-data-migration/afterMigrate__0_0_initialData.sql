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

TRUNCATE TABLE certificate CASCADE;

INSERT INTO certificate (name, points, deleted_at, comment)
VALUES
    ('CompTIA A+', 5.0, NULL, 'Entry-level IT certification covering hardware, software, and troubleshooting'),
    ('Cisco CCNA', 7.5, '2023-06-20 14:21:27.063055', 'Cisco Certified Network Associate – networking fundamentals and routing/switching'),
    ('Microsoft 365 Administrator Associate', 6.0, NULL, 'Cloud and productivity administration in Microsoft 365 environments'),
    ('AWS Certified Solutions Architect – Associate', 8.0, NULL, 'Amazon Web Services cloud design and deployment certification'),
    ('ITIL 4 Foundation', 4.0, NULL, 'Certification in IT service management best practices'),
    ('Google IT Support Professional', 5.5, NULL, 'Foundational IT skills including networking, OS, and security basics'),
    ('Red Hat Certified System Administrator (RHCSA)', 7.0, NULL, 'Linux administration and system management certification'),
    ('Certified Information Systems Security Professional (CISSP)', 9.0, NULL, 'Advanced cybersecurity certification for security leadership roles'),
    ('Project Management Professional (PMP)', 8.5, NULL, 'Globally recognized project management certification'),
    ('Microsoft Certified: Azure Administrator Associate', 7.5, NULL, 'Managing Azure cloud services and resources');

INSERT INTO certificate (name, points, deleted_at, comment, certificate_type)
VALUES
    ('Soldier', 7.5, NULL, '', 'MILITARY_FUNCTION'),
    ('Ski Camp Manager', 5, NULL, '', 'YOUTH_AND_SPORT'),
    ('Leader Essentials', 2, NULL, '', 'LEADERSHIP_TRAINING');