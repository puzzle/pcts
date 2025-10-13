TRUNCATE TABLE organisation_unit CASCADE;
TRUNCATE TABLE member CASCADE;

INSERT INTO organisation_unit (name)
VALUES
    ('/zh'),
    ('/mobility'),
    ('/bbt'),
    ('/mid');

INSERT INTO member (name, last_name, abbreviation, employment_state, date_of_hire, birth_date, organisation_unit)
VALUES
    ('Lena', 'Müller', 'LM', 'MEMBER', '2021-07-15', '1999-08-10', 1),
    ( 'Jonas', 'Schmidt', 'JS', 'MEMBER', '2020-06-01', '1998-03-03', 2),
    ('Sophie', 'Keller', 'SK', 'APPLICANT', '2019-11-22', '1999-02-28', 3),
    ('Tobias', 'Weber', 'TW', 'APPLICANT', '2022-02-14', '2000-07-06', 4),
    ('Mara', 'Becker', 'MB', 'EX_MEMBER', '2023-01-09', '2001-08-05', 1),
    ('Felix', 'Hofmann', 'FH', 'EX_MEMBER', '2021-09-17', '2000-02-29', 1);
