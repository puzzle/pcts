CREATE TABLE IF NOT EXISTS member_role
(
                                    member_id BIGINT NOT NULL,
                                    role_id BIGINT NOT NULL,
                                    PRIMARY KEY (member_id,role_id),
                                    CONSTRAINT member_role_fk_1
                                        FOREIGN KEY (member_id) REFERENCES member (id),
                                    CONSTRAINT member_role_fk_2
                                        FOREIGN KEY (role_id) REFERENCES role (id)
);