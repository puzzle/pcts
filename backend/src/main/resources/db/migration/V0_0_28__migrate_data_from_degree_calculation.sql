INSERT INTO degree_calculation_weight (weight, relevancy, degree_calculation_id)
SELECT weight, relevancy, id
FROM degree_calculation;

ALTER TABLE degree_calculation
    DROP COLUMN weight,
    DROP COLUMN relevancy;