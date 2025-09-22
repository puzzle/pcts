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

TRUNCATE TABLE organisation_unit CASCADE;

INSERT INTO organisation_unit (name)
VALUES
    ('/zh'),
    ('/mobility'),
    ('/bbt'),
    ('/mid');

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

TRUNCATE TABLE experience_type CASCADE;

INSERT INTO experience_type (name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    ('Internship', 4, 2, 1.5),
    ('Volunteer Work', 2, 1.5, 1),
    ('Hackathon', 9, 4,2),
    ('Leadership Role', 13, 7, 5),
    ('Research Project', 6, 5, 3.25),
    ('Coursework Project', 7, 3, 1),
    ('Freelance Work', 7, 6.25, 4),
    ('Professional Job', 4.5, 2, 1);

TRUNCATE TABLE degree_type CASCADE;

INSERT INTO degree_type (name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    ('Bachelor''s Degree', 21.575, 16.5, 11),
    ('Master''s Degree', 25, 17.99, 15.325),
    ('PhD', 16, 13.555, 10.25),
    ('Associate Degree', 11, 9.5, 5.75);
