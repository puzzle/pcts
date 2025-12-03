CREATE TABLE IF NOT EXISTS calculation_experience
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    calculation_id   BIGINT NOT NULL,
    experience_id    BIGINT NOT NULL,
    relevancy        TEXT NOT NULL
);

ALTER TABLE calculation_experience
    ADD CONSTRAINT fk_calculation_experience_calculation
        FOREIGN KEY (calculation_id)
            REFERENCES calculation (id);

ALTER TABLE calculation_experience
    ADD CONSTRAINT fk_calculation_experience_experience
        FOREIGN KEY (experience_id)
            REFERENCES experience (id)