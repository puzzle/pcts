TRUNCATE TABLE role CASCADE;
TRUNCATE TABLE organisation_unit CASCADE;
TRUNCATE TABLE certificate CASCADE;
TRUNCATE TABLE experience_type CASCADE;
TRUNCATE TABLE degree_type CASCADE;
TRUNCATE TABLE tag CASCADE;
TRUNCATE TABLE CERTIFICATE_TAG CASCADE;
TRUNCATE TABLE member CASCADE;
TRUNCATE TABLE degree CASCADE;

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


INSERT INTO certificate (name, points, deleted_at, comment, certificate_type)
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
    ('Internship', 4, 2, 1.5),
    ('Volunteer Work', 2, 1.5, 1),
    ('Hackathon', 9, 4, 2),
    ('Leadership Role', 13, 7, 5),
    ('Research Project', 6, 5, 3.25),
    ('Coursework Project', 7, 3, 1),
    ('Freelance Work', 7, 6.25, 4),
    ('Professional Job', 4.5, 2, 1);


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

INSERT INTO CERTIFICATE_TAG (CERTIFICATE_ID, TAG_ID)
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
    ( 'Mara', 'Becker', 'MB', 'EX_MEMBER', '2023-01-09', '2001-08-05', 1),
    ( 'Felix', 'Hofmann', 'FH', 'EX_MEMBER', '2021-09-17', '2000-02-29', 1);

INSERT INTO degree (member, name, institution, completed, type, start_date, end_date, comment)
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
        TRUE,
        1,
        '2016-09-01',
        '2019-06-30',
        'Focused on linguistics, literature, and academic writing.'),
       (3,
        'Bachelor of Science in Computer Science',
        'EPFL',
        TRUE,
        1,
        '2017-09-01',
        '2020-06-30',
        'Specialized in software engineering and AI.');
