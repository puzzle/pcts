CREATE TABLE IF NOT EXISTS calculation_certificate
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    calculation_id   BIGINT NOT NULL,
    certificate_id   BIGINT NOT NULL
);

ALTER TABLE calculation_certificate
    ADD CONSTRAINT fk_calculation_certificate_calculation
        FOREIGN KEY (calculation_id)
            REFERENCES calculation (id);

ALTER TABLE calculation_certificate
    ADD CONSTRAINT fk_calculation_certificate_certificate
        FOREIGN KEY (certificate_id)
            REFERENCES certificate (id)