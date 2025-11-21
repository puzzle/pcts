CREATE TABLE IF NOT EXISTS experience
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    member_id           BIGINT NOT NULL,
    name                TEXT NOT NULL,
    employer            TEXT,
    percent             INTEGER NOT NULL,
    experience_type_id  BIGINT NOT NULL,
    comment             TEXT,
    start_date          DATE NOT NULL,
    end_date            DATE,
    deleted_at          TIMESTAMP DEFAULT NULL,

    CONSTRAINT fk_experience_member
        FOREIGN KEY (member_id)
            REFERENCES member (id),

    CONSTRAINT fk_experience_experience_type
        FOREIGN KEY (experience_type_id)
        REFERENCES experience_type (id)
);