ALTER TABLE certificate_type
    ADD COLUMN effort decimal NOT NULL DEFAULT 0,
    ADD COLUMN exam_duration int,
    ADD COLUMN link text,
    ADD COLUMN exam_type TEXT NOT NULL DEFAULT 'NONE',
    ADD COLUMN publisher TEXT NOT NULL DEFAULT 'CHANGE ME',
    ADD COLUMN link_error_count int,
    ADD COLUMN link_last_checked_at timestamp;

ALTER TABLE certificate_type
    ALTER COLUMN effort DROP DEFAULT,
    ALTER COLUMN exam_type DROP DEFAULT,
    ALTER COLUMN publisher DROP DEFAULT,
    ADD CONSTRAINT uc_certificate_type_name_publisher UNIQUE (name, publisher);
