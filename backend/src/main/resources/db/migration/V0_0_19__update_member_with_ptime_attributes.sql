ALTER TABLE member
    ADD COLUMN ptime_id bigint UNIQUE,
    ADD COLUMN last_successful_sync timestamp,
    ADD COLUMN sync_error_count int;
