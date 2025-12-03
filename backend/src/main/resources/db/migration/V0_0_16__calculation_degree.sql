CREATE TABLE IF NOT EXISTS calculation_degree
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    calculation_id   BIGINT NOT NULL,
    degree_id        BIGINT NOT NULL,
    weight           NUMERIC NOT NULL,
    relevancy        TEXT NOT NULL
);

ALTER TABLE calculation_degree
    ADD CONSTRAINT fk_calculation_degree_calculation
        FOREIGN KEY (calculation_id)
            REFERENCES calculation (id);

ALTER TABLE calculation_degree
    ADD CONSTRAINT fk_calculation_degree_degree
        FOREIGN KEY (degree_id)
            REFERENCES degree (id)