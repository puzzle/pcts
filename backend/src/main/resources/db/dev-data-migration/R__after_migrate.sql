TRUNCATE TABLE example CASCADE;
TRUNCATE TABLE role CASCADE;
TRUNCATE TABLE organisation_unit CASCADE;
TRUNCATE TABLE certificate CASCADE;
TRUNCATE TABLE experience_type CASCADE;
TRUNCATE TABLE degree_type CASCADE;
TRUNCATE TABLE tag CASCADE;
TRUNCATE TABLE CERTIFICATE_TAG CASCADE;
TRUNCATE TABLE member CASCADE;

INSERT INTO example (id, text)
VALUES (1, 'Example 1'),
       (2, 'Example 2'),
       (3, 'Example 3'),
       (4, 'Example 4'),
       (5, 'Example 5');

SELECT setval('sequence_example', (SELECT MAX(id) FROM example));


INSERT INTO role (id, name, is_management)
VALUES
    (1, 'Administrator', TRUE),
    (2, 'Manager', TRUE),
    (3, 'Team Lead', TRUE),
    (4, 'Developer', FALSE),
    (5, 'Intern', FALSE),
    (6, 'Extern', FALSE),
    (7, 'Consultant', FALSE);


INSERT INTO organisation_unit (id, name)
VALUES
    (1, '/zh'),
    (2, '/mobility'),
    (3, '/bbt'),
    (4, '/mid');


INSERT INTO certificate (id, name, points, deleted_at, comment)
VALUES
    (1, 'CompTIA A+', 5.0, NULL, 'Entry-level IT certification covering hardware, software, and troubleshooting'),
    (2, 'Cisco CCNA', 7.5, '2023-06-20 14:21:27.063055', 'Cisco Certified Network Associate – networking fundamentals and routing/switching'),
    (3, 'Microsoft 365 Administrator Associate', 6.0, NULL, 'Cloud and productivity administration in Microsoft 365 environments'),
    (4, 'AWS Certified Solutions Architect – Associate', 8.0, NULL, 'Amazon Web Services cloud design and deployment certification'),
    (5, 'ITIL 4 Foundation', 4.0, NULL, 'Certification in IT service management best practices'),
    (6, 'Google IT Support Professional', 5.5, NULL, 'Foundational IT skills including networking, OS, and security basics'),
    (7, 'Red Hat Certified System Administrator (RHCSA)', 7.0, NULL, 'Linux administration and system management certification'),
    (8, 'Certified Information Systems Security Professional (CISSP)', 9.0, NULL, 'Advanced cybersecurity certification for security leadership roles'),
    (9, 'Project Management Professional (PMP)', 8.5, NULL, 'Globally recognized project management certification'),
    (10, 'Microsoft Certified: Azure Administrator Associate', 7.5, NULL, 'Managing Azure cloud services and resources');


INSERT INTO experience_type (id, name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    (1, 'Internship', 4, 2, 1.5),
    (2, 'Volunteer Work', 2, 1.5, 1),
    (3, 'Hackathon', 9, 4, 2),
    (4, 'Leadership Role', 13, 7, 5),
    (5, 'Research Project', 6, 5, 3.25),
    (6, 'Coursework Project', 7, 3, 1),
    (7, 'Freelance Work', 7, 6.25, 4),
    (8, 'Professional Job', 4.5, 2, 1);


INSERT INTO degree_type (id, name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    (1, 'Bachelor''s Degree', 21.575, 16.5, 11),
    (2, 'Master''s Degree', 25, 17.99, 15.325),
    (3, 'PhD', 16, 13.555, 10.25),
    (4, 'Associate Degree', 11, 9.5, 5.75);


INSERT INTO tag (id, name)
VALUES
    (1, 'Machine learning'),
    (2, 'GitLab Sales Core'),
    (3, 'AWSA01'),
    (4, 'Berufsbildner'),
    (5, 'GitLab 101');

INSERT INTO CERTIFICATE_TAG (CERTIFICATE_ID, TAG_ID)
VALUES
    (1, 1),
    (3, 2),
    (4, 3),
    (7, 4),
    (10, 4),
    (10, 5);

INSERT INTO member (id, name, last_name, abbreviation, employment_state, date_of_hire, birth_date, is_admin, organisation_unit)
VALUES
    (1, 'Lena', 'Müller', 'LM', 'MEMBER', '2021-07-15', '1999-08-10', TRUE, 1),
    (2, 'Jonas', 'Schmidt', 'JS', 'MEMBER', '2020-06-01', '1998-03-03', FALSE, 2),
    (3, 'Sophie', 'Keller', 'SK', 'APPLICANT', '2019-11-22', '1999-02-28', FALSE, 3),
    (4, 'Tobias', 'Weber', 'TW', 'APPLICANT', '2022-02-14', '2000-07-06', FALSE, 4),
    (5, 'Mara', 'Becker', 'MB', 'EX_MEMBER', '2023-01-09', '2001-08-05', FALSE, 1),
    (6, 'Felix', 'Hofmann', 'FH', 'EX_MEMBER', '2021-09-17', '2000-02-29', FALSE, 1);
