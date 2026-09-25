Create TABLE degree_calculation_weight (
                                           id BIGINT GENERATED ALWAYS As IDENTITY PRIMARY KEY NOT NULL,
                                           weight SMALLINT NOT NULL CHECK (weight BETWEEN 0 AND 100),
                                           relevancy TEXT NOT NULL,
                                           degree_calculation_id BIGINT NOT NULL,
                                           UNIQUE (degree_calculation_id, relevancy),
                                           CONSTRAINT fk_degree_calculation
                                               FOREIGN KEY (degree_calculation_id)
                                                   references degree_calculation (id)
                                       );
