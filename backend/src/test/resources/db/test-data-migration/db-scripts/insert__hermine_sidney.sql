INSERT INTO role(name, is_management)
VALUES ('M2 Division Manager', TRUE);

INSERT INTO member(first_name, last_name, abbreviation, employment_state, date_of_hire, birth_date, organisation_unit)
VALUES (' Hermine', 'Sidney', 'HS', 'MEMBER', '2014-08-01', '1973-06-22', 2);

INSERT INTO certificate_type(name, points, certificate_kind)
VALUES ('Coaching (single)', 1, 'LEADERSHIP_TRAINING'),
       ('Coaching (team)', 0.5, 'LEADERSHIP_TRAINING'),
       ('Integral Coach Living Sense', 1, 'LEADERSHIP_TRAINING'),
       ('Life Coach', 1, 'LEADERSHIP_TRAINING'),
       ('Leadership training', 0.5, 'YOUTH_AND_SPORT');

INSERT INTO certificate(member_id, certificate_type_id)
VALUES (1000, 1000),
       (1000, 1001),
       (1000, 1002),
       (1000, 1003),
       (1000, 1004);

INSERT INTO experience_type(name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES ('Work experience', 3, 2, 1),
       ('Side hustle', 3, 2, 1);

INSERT INTO experience(member_id, name, employer, percent, experience_type_id, start_date, end_date)
VALUES (1000, 'Exchange year', 'Welsch', 100, 1000, '1989-07-01', '1990-06-30'), -- 1000
       (1000, 'Advisor', 'Bynex', 100, 1000, '1993-08-01', '1994-12-31'), -- 1001
       (1000, 'Trip', 'Asia', 100, 1001, '1995-01-01', '1995-06-30'), -- 1002
       (1000, 'Project assistent', 'Ondatech', 100, 1000, '1995-07-01', '1997-06-30'), -- 1003
       (1000, 'Partproject leader', 'Ondatech', 100, 1000, '1997-07-01', '2000-02-29'), -- 1004
       (1000, 'First level suppport', 'Ondatech', 100, 1000, '2000-03-01', '2000-09-30'), -- 1005
       (1000, 'Trip', 'Idk', 100, 1001, '2000-10-01', '2000-12-31'), -- 1006
       (1000, 'HR consultant', 'Indatech', 100, 1000, '2001-01-01', '2003-10-31'), -- 1007
       (1000, 'HR consultant', 'Indatech', 60, 1000, '2003-11-01', '2006-03-31'), -- 1008
       (1000, 'PL', 'Indatech', 40, 1000, '2003-11-01', '2006-03-31'), -- 1009
       (1000, 'HR consultant', 'Indatech', 50, 1000, '2006-04-01', '2014-01-31'), -- 1010
       (1000, 'PL', 'Indatech', 40, 1000, '2006-04-01', '2014-01-31'), -- 1011
       (1000, 'Life experience', 'Indatech', 10, 1001, '2006-04-01', '2014-01-31'), -- 1012
       (1000, 'Trip', 'Idk', 100, 1001, '2014-02-01', '2014-07-31'), -- 1013
       (1000, 'Head of HR', 'Puzzle', 80, 1000, '2014-08-01', '2018-06-30'), -- 1014
       (1000, 'Life experience', 'Puzzle', 20, 1001, '2014-08-01', '2018-06-30'), -- 1015
       (1000, 'Head of HR', 'Puzzle', 90, 1000, '2018-07-01', '2023-03-31'), -- 1016
       (1000, 'Life experience', 'Puzzle', 10, 1001, '2018-07-01', '2023-03-31'), -- 1017
       (1000, 'Head of HR', 'Puzzle', 80, 1000, '2023-04-01', '2023-12-31'), -- 1018
       (1000, 'Life experience', 'Puzzle', 20, 1001, '2023-04-01', '2023-12-31'), -- 1019
       (1000, 'Head of HR', 'Puzzle', 90, 1000, '2024-01-01', '2025-12-31'), -- 1020
       (1000, 'Life experience', 'Puzzle', 10, 1001, '2024-01-01', '2025-12-31');  -- 1021

INSERT INTO degree_type(name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES ('Apprenticeship', 5, 4, 3),
       ('Specialist', 8, 5.33, 2.67);

INSERT INTO degree(member_id, name, institution, completed, degree_type_id, start_date, end_date)
VALUES (1000, 'Apprenticeship', 'AnzuLabs', true, 1000, '2000-02-03', null),
       (1000, 'Specialist', 'Viacet', true, 1001, '2005-01-01', null);

INSERT INTO calculation(member_id, role_id, state, publication_date, publicized_by)
VALUES (1000, 1000, 'ACTIVE', '2020-01-01', 'root');

INSERT INTO certificate_calculation(calculation_id, certificate_id)
VALUES (1000, 1000),
       (1000, 1001),
       (1000, 1002),
       (1000, 1003),
       (1000, 1004);

INSERT INTO experience_calculation(calculation_id, experience_id, relevancy)
VALUES (1000, 1000, 'POORLY'),
       (1000, 1001, 'NORMAL'),
       (1000, 1002, 'POORLY'),
       (1000, 1003, 'NORMAL'),
       (1000, 1004, 'STRONGLY'),
       (1000, 1005, 'NORMAL'),
       (1000, 1006, 'POORLY'),
       (1000, 1007, 'NORMAL'),
       (1000, 1008, 'NORMAL'),
       (1000, 1009, 'STRONGLY'),
       (1000, 1010, 'NORMAL'),
       (1000, 1011, 'STRONGLY'),
       (1000, 1012, 'POORLY'),
       (1000, 1013, 'POORLY'),
       (1000, 1014, 'STRONGLY'),
       (1000, 1015, 'POORLY'),
       (1000, 1016, 'STRONGLY'),
       (1000, 1017, 'POORLY'),
       (1000, 1018, 'STRONGLY'),
       (1000, 1019, 'POORLY'),
       (1000, 1020, 'STRONGLY'),
       (1000, 1021, 'POORLY');

INSERT INTO degree_calculation(calculation_id, degree_id, weight, relevancy)
VALUES (1000, 1000, 100, 'STRONGLY'),
       (1000, 1001, 100, 'STRONGLY');
