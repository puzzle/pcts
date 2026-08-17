CREATE TABLE IF NOT EXISTS member_role
(                                   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
                                    member_id BIGINT NOT NULL,
                                    role_id BIGINT NOT NULL,
                                    deleted_at TIMESTAMP DEFAULT NULL,
                                    CONSTRAINT member_role_fk_1
                                        FOREIGN KEY (member_id) REFERENCES member (id),
                                    CONSTRAINT member_role_fk_2
                                        FOREIGN KEY (role_id) REFERENCES role (id)
);