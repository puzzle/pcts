INSERT INTO member_role (member_id, role_id)
SELECT DISTINCT member_id, role_id
FROM calculation
WHERE state NOT LIKE 'ARCHIVED';