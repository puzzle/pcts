ALTER TABLE experience
    ADD CONSTRAINT fk_experience_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE experience
    ADD CONSTRAINT fk_experience_experience_type
        FOREIGN KEY (experience_type_id)
            REFERENCES experience_type (id);

ALTER TABLE member
    ADD CONSTRAINT fk_member_organisation_unit
        FOREIGN KEY (organisation_unit)
            REFERENCES organisation_unit (id);

ALTER TABLE degree
    ADD CONSTRAINT fk_degree_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE degree
    ADD CONSTRAINT fk_degree_degree_type
        FOREIGN KEY (degree_type_id)
            REFERENCES degree_type (id);

ALTER TABLE certificate
    ADD CONSTRAINT fk_certificate_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE certificate
    ADD CONSTRAINT fk_certificate_certificate_type
        FOREIGN KEY (certificate_type_id)
            REFERENCES certificate_type (id)