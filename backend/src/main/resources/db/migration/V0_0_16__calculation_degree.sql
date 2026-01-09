CREATE TABLE IF NOT EXISTS degree_calculation
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    calculation_id   BIGINT NOT NULL,
    degree_id        BIGINT NOT NULL,
    weight           NUMERIC NOT NULL,
    relevancy        TEXT NOT NULL,
    comment          TEXT

);

ALTER TABLE degree_calculation
    ADD CONSTRAINT fk_degree_calculation_calculation
        FOREIGN KEY (calculation_id)
            REFERENCES calculation (id);

ALTER TABLE degree_calculation
    ADD CONSTRAINT fk_degree_calculation_degree
        FOREIGN KEY (degree_id)
            REFERENCES degree (id)