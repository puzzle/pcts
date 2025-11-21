CREATE TABLE IF NOT EXISTS degree
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    member_id           BIGINT NOT NULL,
    name                TEXT NOT NULL,
    institution         TEXT,
    completed           BOOLEAN NOT NULL,
    degree_type_id      BIGINT NOT NULL,
    start_date          DATE NOT NULL,
    end_date            DATE,
    deleted_at          TIMESTAMP DEFAULT NULL,
    comment             TEXT,

    CONSTRAINT fk_degree_member
        FOREIGN KEY (member_id)
            REFERENCES member (id),

    CONSTRAINT fk_degree_degree_type
        FOREIGN KEY (degree_type_id)
            REFERENCES degree_type (id)
);