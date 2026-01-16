TRUNCATE TABLE role CASCADE;
TRUNCATE TABLE organisation_unit CASCADE;
TRUNCATE TABLE certificate_type CASCADE;
TRUNCATE TABLE experience_type CASCADE;
TRUNCATE TABLE degree_type CASCADE;
TRUNCATE TABLE tag CASCADE;
TRUNCATE TABLE CERTIFICATE_TYPE_TAG CASCADE;
TRUNCATE TABLE member CASCADE;
TRUNCATE TABLE experience CASCADE;
TRUNCATE TABLE degree CASCADE;
TRUNCATE TABLE certificate CASCADE;
TRUNCATE TABLE calculation CASCADE;

INSERT INTO role (name, is_management)
VALUES
    ('Administrator', TRUE),
    ('Manager', TRUE),
    ('Team Lead', TRUE),
    ('Developer', FALSE),
    ('Intern', FALSE),
    ('Extern', FALSE),
    ('Consultant', FALSE);


INSERT INTO organisation_unit (name)
VALUES
    ('/zh'),
    ('/mobility'),
    ('/bbt'),
    ('/mid');


INSERT INTO certificate_type (name, points, deleted_at, comment, certificate_kind)
VALUES
    ('CompTIA A+', 5.0, NULL, 'Entry-level IT certification covering hardware, software, and troubleshooting', 'CERTIFICATE'),
    ('Cisco CCNA', 7.5, '2023-06-20 14:21:27.063055', 'Cisco Certified Network Associate – networking fundamentals and routing/switching', 'CERTIFICATE'),
    ('Microsoft 365 Administrator Associate', 6.0, NULL, 'Cloud and productivity administration in Microsoft 365 environments', 'CERTIFICATE'),
    ('AWS Certified Solutions Architect – Associate', 8.0, NULL, 'Amazon Web Services cloud design and deployment certification', 'CERTIFICATE'),
    ('ITIL 4 Foundation', 4.0, NULL, 'Certification in IT service management best practices', 'CERTIFICATE'),
    ('Google IT Support Professional', 5.5, NULL, 'Foundational IT skills including networking, OS, and security basics', 'CERTIFICATE'),
    ('Red Hat Certified System Administrator (RHCSA)', 7.0, NULL, 'Linux administration and system management certification', 'CERTIFICATE'),
    ('Certified Information Systems Security Professional (CISSP)', 9.0, NULL, 'Advanced cybersecurity certification for security leadership roles', 'CERTIFICATE'),
    ('Project Management Professional (PMP)', 8.5, NULL, 'Globally recognized project management certification', 'CERTIFICATE'),
    ('Microsoft Certified: Azure Administrator Associate', 7.5, NULL, 'Managing Azure cloud services and resources', 'CERTIFICATE'),

    --     Below are all the LeadershipExperiences
    ('Soldier', 7.5, NULL, '', 'MILITARY_FUNCTION'),
    ('Ski Camp Manager', 5, NULL, '', 'YOUTH_AND_SPORT'),
    ('Leader Essentials', 2, NULL, '', 'LEADERSHIP_TRAINING');


INSERT INTO experience_type (name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    ('Praktikum', 2, 1.33, 0.67),
    ('Berufserfahrung', 3, 2, 1),
    ('Nebenerwerb', 3, 2,1);


INSERT INTO degree_type (name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES
    ('Bachelor''s Degree', 21.575, 16.5, 11),
    ('Master''s Degree', 25, 17.99, 15.325),
    ('PhD', 16, 13.555, 10.25),
    ('Associate Degree', 11, 9.5, 5.75);


INSERT INTO tag (name)
VALUES
    ('Machine learning'),
    ('GitLab Sales Core'),
    ('AWSA01'),
    ('Berufsbildner'),
    ('GitLab 101');

INSERT INTO CERTIFICATE_TYPE_TAG (CERTIFICATE_TYPE_ID, TAG_ID)
VALUES
    (1, 1),
    (3, 2),
    (4, 3),
    (7, 4),
    (10, 4),
    (10, 5);

INSERT INTO member (first_name, last_name, abbreviation, employment_state, date_of_hire, birth_date, organisation_unit)
VALUES
    ('Lena', 'Müller', 'LM', 'MEMBER', '2021-07-15', '1999-08-10', 1),
    ('Jonas', 'Schmidt', 'JS', 'MEMBER', '2020-06-01', '1998-03-03', 2),
    ( 'Sophie', 'Keller', 'SK', 'APPLICANT', '2019-11-22', '1999-02-28', 3),
    ( 'Tobias', 'Weber', 'TW', 'APPLICANT', '2022-02-14', '2000-07-06', 4),
    ( 'Mara', 'Becker', 'MB', 'EX_MEMBER', '2023-01-09', '2001-08-05', null),
    ( 'Felix', 'Hofmann', 'FH', 'EX_MEMBER', '2021-09-17', '2000-02-29', 1);

INSERT INTO experience (member_id, name, employer, percent, experience_type_id, comment, start_date, end_date)
VALUES
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (1, 'Software Engineer', 'TechNova Solutions', 100, 1, 'Worked on backend APIs and DevOps tasks.', '2019-03-01', '2022-07-31'),
    (2, 'Marketing Intern', null, 80, 2, 'Assisted in content strategy and social media analytics.', '2021-06-01', null),
    (1, 'Web Developer (Freelance)', 'Freelance', 50, 3, null, '2020-02-15', '2020-12-15'),
    (3, 'Data Analyst',  null, 100, 1, null, '2018-01-10', null);

INSERT INTO degree (member_id, name, institution, completed, degree_type_id, start_date, end_date, comment)
VALUES (1,
        'Bachelor of Science in Mathematics',
        'ETH Zürich',
        TRUE,
        1,
        '2015-09-01',
        '2018-06-30',
        'Strong focus on applied and pure mathematics.'),
       (2,
        'Bachelor of Arts in English Language and Literature',
        'University of Geneva',
        FALSE,
        3,
        '2016-09-01',
        '2019-06-30',
        'Focused on linguistics, literature, and academic writing.'),
       (3,
        'Bachelor of Science in Computer Science',
        'EPFL',
        TRUE,
        4,
        '2017-09-01',
        '2020-06-30',
        null);

INSERT INTO certificate
(member_id, certificate_type_id, completed_at, valid_until, comment, deleted_at)
VALUES
    (1, 1, '2023-01-15', '2025-01-14', 'Completed first aid training.', NULL),
    (2, 2, '2022-11-01', NULL, 'Lifetime certification.', NULL),
    (3, 3, '2020-03-22', '2022-03-21', 'Needs renewal.', NULL),
    (4, 1, '2021-06-10', '2023-06-09', 'Left organization.', '2023-07-01 10:00:00'),
    (5, 4, '2024-02-01', '2026-02-01', NULL, NULL),

    --     LeadershipExperiences
    (1, 11, NULL, NULL, NULL, NULL),
    (2, 12, NULL, NULL, 'This is quite hard', NULL),
    (4, 13, NULL, NULL, NULL, '2025-01-03 10:43:12');

INSERT INTO calculation (member_id, role_id, state, publication_date, publicized_by)
VALUES
    (1, 1, 'ACTIVE', '2025-02-02', 'system'),
    (2, 2, 'DRAFT', NULL, NULL),
    (3, 3, 'ARCHIVED', '2025-01-10', 'admin_user'),
    (4, 4, 'DRAFT', NULL, NULL),
    (5, 5, 'ACTIVE', '2025-01-28', 'admin_user');

TRUNCATE TABLE experience_calculation CASCADE;

INSERT INTO experience_calculation
(calculation_id, experience_id, relevancy, comment)
VALUES
    (1, 1, 'HIGHLY',  'Core backend and DevOps experience'),
    (1, 3, 'LIMITED', 'Freelance work partially relevant'),
    (2, 2, 'LITTLE',  'Marketing internship, low relevance'),
    (3, 4, 'HIGHLY',  'Full-time data analyst role'),
    (4, 2, 'LIMITED', 'Internship experience');

TRUNCATE TABLE degree_calculation CASCADE;

INSERT INTO degree_calculation
(calculation_id, degree_id, weight, relevancy, comment)
VALUES
    (1, 1, 100, 'HIGHLY',  'Strong analytical background'),
    (2, 2, 40,  'LITTLE',  'Low relevance for technical role'),
    (3, 3, 90,  'HIGHLY',  'Directly relevant degree'),
    (4, 3, 70,  'LIMITED', 'Relevant but less practical focus');

TRUNCATE TABLE certificate_calculation CASCADE;

INSERT INTO certificate_calculation
(calculation_id, certificate_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (1, 6),
    (2, 7),
    (4, 8);
