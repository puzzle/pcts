ALTER TABLE certificate_type
    ADD COLUMN effort decimal DEFAULT 0, -- TODO: Add NOT NULL once leadership_type is separated
    ADD COLUMN exam_duration int,
    ADD COLUMN link text,
    ADD COLUMN exam_type TEXT DEFAULT 'NONE', -- TODO: Add NOT NULL once leadership_type is separated
    ADD COLUMN publisher TEXT DEFAULT 'CHANGE ME', -- TODO: Add NOT NULL once leadership_type is separated
    ADD COLUMN link_error_count int DEFAULT 0,
    ADD COLUMN link_last_checked_at timestamp;

ALTER TABLE certificate_type
    ALTER COLUMN effort DROP DEFAULT,
    ALTER COLUMN exam_type DROP DEFAULT,
    ALTER COLUMN publisher DROP DEFAULT,
    ADD CONSTRAINT uc_certificate_type_name_publisher UNIQUE (name, publisher);
