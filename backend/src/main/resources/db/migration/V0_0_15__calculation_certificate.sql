CREATE TABLE IF NOT EXISTS certificate_calculation
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    calculation_id   BIGINT NOT NULL,
    certificate_id   BIGINT NOT NULL
);

ALTER TABLE certificate_calculation
    ADD CONSTRAINT fk_certificate_calculation_calculation
        FOREIGN KEY (calculation_id)
            REFERENCES calculation (id);

ALTER TABLE certificate_calculation
    ADD CONSTRAINT fk_certificate_calculation_certificate
        FOREIGN KEY (certificate_id)
            REFERENCES certificate (id)