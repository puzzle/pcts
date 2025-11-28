CREATE TABLE IF NOT EXISTS calculation
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    member_id        BIGINT NOT NULL,
    role_id          BIGINT NOT NULL,
    state            TEXT   NOT NULL,
    publication_date DATE,
    publicized_by    TEXT
);

ALTER TABLE calculation
    ADD CONSTRAINT fk_calculation_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE calculation
    ADD CONSTRAINT fk_calculation_role
        FOREIGN KEY (role_id)
            REFERENCES role (id)