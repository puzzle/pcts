Create TABLE degree_calculation_weight (
                                           id BIGINT GENERATED ALWAYS As IDENTITY PRIMARY KEY NOT NULL,
                                           weight NUMERIC NOT NULL,
                                           relevancy TEXT NOT NULL,
                                           degree_calculation_id BIGINT NOT NULL,
                                           UNIQUE (degree_calculation_id, relevancy),
                                           CONSTRAINT fk_degree_calculation
                                               FOREIGN KEY (degree_calculation_id)
                                                   references degree_calculation (id)
                                       );

Alter Table degree_calculation
    add column degree_calculation_weight_id bigint default null;

Alter table degree_calculation
    add constraint degree_calc_fk
        foreign key (degree_calc_weight_id) references degree_calculation_weight (id)