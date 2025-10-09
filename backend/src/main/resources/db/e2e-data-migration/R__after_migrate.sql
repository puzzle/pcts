TRUNCATE TABLE organisation_unit CASCADE;
TRUNCATE TABLE member CASCADE;

INSERT INTO organisation_unit (id, name)
VALUES
    (1, '/zh'),
    (2, '/mobility'),
    (3, '/bbt'),
    (4, '/mid');

INSERT INTO member (id, name, last_name, abbreviation, employment_state, date_of_hire, birth_date, is_admin, organisation_unit)
VALUES
    (1, 'Lena', 'Müller', 'LM', 'MEMBER', '2021-07-15', '1999-08-10', TRUE, 1),
    (2, 'Jonas', 'Schmidt', 'JS', 'MEMBER', '2020-06-01', '1998-03-03', FALSE, 2),
    (3, 'Sophie', 'Keller', 'SK', 'APPLICANT', '2019-11-22', '1999-02-28', FALSE, 3),
    (4, 'Tobias', 'Weber', 'TW', 'APPLICANT', '2022-02-14', '2000-07-06', FALSE, 4),
    (5, 'Mara', 'Becker', 'MB', 'EX_MEMBER', '2023-01-09', '2001-08-05', FALSE, 1),
    (6, 'Felix', 'Hofmann', 'FH', 'EX_MEMBER', '2021-09-17', '2000-02-29', FALSE, 1);
