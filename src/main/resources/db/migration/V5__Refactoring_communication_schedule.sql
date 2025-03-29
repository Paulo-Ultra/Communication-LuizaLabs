ALTER TABLE communication_schedules MODIFY guid BINARY(16);
ALTER TABLE communication_schedules ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
