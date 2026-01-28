INSERT INTO role(name, is_management)
VALUES ('M1 Project Manager', TRUE),
       ('M2 Division Manager', TRUE);

INSERT INTO member(first_name, last_name, abbreviation, employment_state, date_of_hire, birth_date, organisation_unit)
VALUES ('Devon', 'Ricky', 'DR', 'MEMBER', '2015-03-17', '1979-11-05', 1);

INSERT INTO certificate_type(name, points, certificate_kind)
VALUES ('Scrum Master', 0.5, 'CERTIFICATE'),
       ('Hermes Foundation', 0.5, 'CERTIFICATE'),
       ('Customer Experience Facilitator', 0.5, 'CERTIFICATE'),
       ('Leadership Handball', 0.5, 'YOUTH_AND_SPORT'),
       ('Periodic Leadership', 0.2, 'YOUTH_AND_SPORT'),
       ('Expert', 1.5, 'YOUTH_AND_SPORT');

INSERT INTO certificate(member_id, certificate_type_id, completed_at, valid_until)
VALUES (1000, 1000, '2021-08-01', null),
       (1000, 1001, '2022-02-01', null),
       (1000, 1002, '2023-12-01', null),
       (1000, 1003, '2023-12-01', null),
       (1000, 1004, '2023-12-01', null),
       (1000, 1005, '2023-12-01', null);

INSERT INTO experience_type(name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES ('Work experience', 3, 2, 1),
       ('Side hustle', 3, 2, 1);

INSERT INTO experience(member_id, name, employer, percent, experience_type_id, start_date, end_date)
VALUES (1000, 'Customs administration', 'Customs specialist', 100, 1000, '1999-07-01', '2000-06-30'), -- 1000
       (1000, 'Childcare', 'Zivi', 100, 1000, '2000-07-01', '2000-12-31'), -- 1001
       (1000, 'Customs administration', 'Customs specialist', 100, 1000, '2001-01-01', '2001-07-31'), -- 1002
       (1000, 'Exhibition supervisor', 'Zivi', 100, 1001, '2002-08-01', '2002-12-31'), -- 1003
       (1000, 'Export specialist', 'Zivi', 100, 1000, '2003-01-01', '2003-05-31'), -- 1004
       (1000, 'Break', '-', 100, 1001, '2003-06-01', '2003-08-31'), -- 1005
       (1000, 'Supply Chain Coordinator', 'Venturion', 100, 1000, '2003-09-01', '2006-10-31'), -- 1006
       (1000, 'Cab driver', 'Taxi', 20, 1000, '2006-11-01', '2008-12-31'), -- 1007
       (1000, 'Factory tours', 'Bellagio AG', 20, 1000, '2006-09-01', '2010-10-31'), -- 1008
       (1000, 'Bicycle courier', 'Bicycle courier', 20, 1000, '2007-01-01', '2007-07-31'), -- 1009
       (1000, 'Various short deployments', 'Zivi', 20, 1000, '2007-04-01', '2010-07-31'), -- 1010
       (1000, 'School sport', 'School', 20, 1000, '2007-08-01', '2008-07-31'), -- 1011
       (1000, 'Teacher', 'School', 80, 1000, '2009-08-01', '2011-07-31'), -- 1012
       (1000, 'Life experience', 'Life itself', 73, 1001, '2006-11-01', '2011-07-31'), -- 1013
       (1000, 'Life experience', 'Life itself', 100, 1001, '2011-08-01', '2011-09-30'), -- 1014
       (1000, 'Specialized Teacher', 'School', 60, 1000, '2011-10-01', '2021-07-31'), -- 1015
       (1000, 'Specialized Teacher (Info)', 'School', 30, 1000, '2011-10-01', '2021-07-31'), -- 1016
       (1000, 'Life experience', 'Life itself', 10, 1001, '2011-10-01', '2013-06-30'), -- 1017
       (1000, 'Administrator', 'Admin inc', 10, 1000, '2013-07-01', '2019-11-30'), -- 1018
       (1000, 'Life experience', 'Life itself', 10, 1001, '2019-12-01', '2021-07-31'), -- 1019
       (1000, 'Project Manager / Division Manager', 'Puzzle', 80, 1001, '2021-08-01', '2025-12-31'), -- 1020
       (1000, 'Life experience', 'Life itself', 20, 1001, '2021-08-01', '2025-12-31'); -- 1021


INSERT INTO degree_type(name, highly_relevant_points, limited_relevant_points, little_relevant_points)
VALUES ('BM', 2, 2, 2),
       ('Commercial/middle school', 4, 4, 4),
       ('Specialist', 8, 5.33, 2.67),
       ('BSc, BBA', 16, 10.67, 5.33),
       ('MSc', 8, 5.33, 2.67),
       ('NDS', 5, 3.33, 1.67);

INSERT INTO degree(member_id, name, institution, completed, degree_type_id, start_date, end_date)
VALUES (1000, 'BM', 'School', true, 1000, '2019-02-03', null),
       (1000, 'Commercial/middle school', 'School', true, 1001, '2021-02-03', null),
       (1000, 'Specialist', 'NovaTips', true, 1002, '2024-02-03', null),
       (1000, 'BSc, BBA', 'School', true, 1003,'2005-02-03', null),
       (1000, 'MSc', 'School', true, 1004, '2006-02-03', null),
       (1000, 'NDS', 'Bynex', true, 1005, '2025-02-03', null);

INSERT INTO calculation(member_id, role_id, state, publication_date, publicized_by)
VALUES (1000, 1000, 'ACTIVE', '2020-01-01', 'root'),
       (1000, 1001, 'ACTIVE', '2018-05-14', 'root');

INSERT INTO certificate_calculation(calculation_id, certificate_id)
VALUES (1000, 1000),
       (1000, 1001),
       (1000, 1002),
       (1000, 1003),
       (1000, 1004),
       (1000, 1005),

       (1001, 1000),
       (1001, 1001),
       (1001, 1002),
       (1001, 1003),
       (1001, 1004),
       (1001, 1005);

INSERT INTO experience_calculation(calculation_id, experience_id, relevancy)
VALUES (1000, 1000, 'NORMAL'),
       (1000, 1001, 'POORLY'),
       (1000, 1002, 'NORMAL'),
       (1000, 1003, 'POORLY'),
       (1000, 1004, 'NORMAL'),
       (1000, 1005, 'POORLY'),
       (1000, 1006, 'NORMAL'),
       (1000, 1007, 'POORLY'),
       (1000, 1008, 'POORLY'),
       (1000, 1009, 'POORLY'),
       (1000, 1010, 'POORLY'),
       (1000, 1011, 'POORLY'),
       (1000, 1012, 'POORLY'),
       (1000, 1013, 'POORLY'),
       (1000, 1014, 'POORLY'),
       (1000, 1015, 'NORMAL'),
       (1000, 1016, 'STRONGLY'),
       (1000, 1017, 'POORLY'),
       (1000, 1018, 'POORLY'),
       (1000, 1019, 'POORLY'),
       (1000, 1020, 'STRONGLY'),
       (1000, 1021, 'POORLY'),

       (1001, 1000, 'NORMAL'),
       (1001, 1001, 'POORLY'),
       (1001, 1002, 'NORMAL'),
       (1001, 1003, 'POORLY'),
       (1001, 1004, 'NORMAL'),
       (1001, 1005, 'POORLY'),
       (1001, 1006, 'NORMAL'),
       (1001, 1007, 'POORLY'),
       (1001, 1008, 'POORLY'),
       (1001, 1009, 'POORLY'),
       (1001, 1010, 'POORLY'),
       (1001, 1011, 'POORLY'),
       (1001, 1012, 'NORMAL'),
       (1001, 1013, 'POORLY'),
       (1001, 1014, 'POORLY'),
       (1001, 1015, 'NORMAL'),
       (1001, 1016, 'NORMAL'),
       (1001, 1017, 'POORLY'),
       (1001, 1018, 'POORLY'),
       (1001, 1019, 'POORLY'),
       (1001, 1020, 'STRONGLY'),
       (1001, 1021, 'POORLY');

INSERT INTO degree_calculation(calculation_id, degree_id, weight, relevancy)
VALUES (1000, 1000, 100, 'STRONGLY'),
       (1000, 1001, 100, 'NORMAL'),
       (1000, 1002, 100, 'NORMAL'),
       (1000, 1003, 100, 'NORMAL'),
       (1000, 1004, 100, 'NORMAL'),
       (1000, 1005, 100, 'STRONGLY'),

       (1001, 1000, 100, 'STRONGLY'),
       (1001, 1001, 100, 'NORMAL'),
       (1001, 1002, 100, 'NORMAL'),
       (1001, 1003, 100, 'NORMAL'),
       (1001, 1004, 100, 'NORMAL'),
       (1001, 1005, 100, 'NORMAL');
