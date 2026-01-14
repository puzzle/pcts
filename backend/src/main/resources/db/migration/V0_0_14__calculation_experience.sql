CREATE TABLE IF NOT EXISTS experience_calculation
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    calculation_id   BIGINT NOT NULL,
    experience_id    BIGINT NOT NULL,
    relevancy        TEXT NOT NULL,
    comment          TEXT
);

ALTER TABLE experience_calculation
    ADD CONSTRAINT fk_experience_calculation_calculation
        FOREIGN KEY (calculation_id)
            REFERENCES calculation (id);

ALTER TABLE experience_calculation
    ADD CONSTRAINT fk_experience_calculation_experience
        FOREIGN KEY (experience_id)
            REFERENCES experience (id)