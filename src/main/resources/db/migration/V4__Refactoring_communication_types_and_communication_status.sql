CREATE TABLE new_communication_types (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         code VARCHAR(50) NOT NULL UNIQUE,
                                         description VARCHAR(255) NOT NULL,
                                         active BOOLEAN DEFAULT TRUE
);

CREATE TABLE new_communication_statuses (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            code VARCHAR(50) NOT NULL UNIQUE,
                                            description VARCHAR(255) NOT NULL,
                                            is_final_state BOOLEAN DEFAULT FALSE
);

INSERT INTO new_communication_types (code, description, active)
SELECT code, description, active FROM communication_types;

INSERT INTO new_communication_statuses (code, description, is_final_state)
SELECT code, description, is_final_state FROM communication_statuses;

ALTER TABLE communication_schedules
    DROP FOREIGN KEY communication_schedules_ibfk_1,
    DROP FOREIGN KEY communication_schedules_ibfk_2;

ALTER TABLE communication_schedules
    ADD COLUMN type_id BIGINT NOT NULL,
    ADD COLUMN status_id BIGINT NOT NULL,
    ADD FOREIGN KEY (type_id) REFERENCES new_communication_types(id),
    ADD FOREIGN KEY (status_id) REFERENCES new_communication_statuses(id);

UPDATE communication_schedules cs
    JOIN new_communication_types nt ON cs.type_guid = nt.code
SET cs.type_id = nt.id;

UPDATE communication_schedules cs
    JOIN new_communication_statuses ns ON cs.status_guid = ns.code
SET cs.status_id = ns.id;

ALTER TABLE communication_schedules
    DROP COLUMN type_guid,
    DROP COLUMN status_guid;

RENAME TABLE communication_types TO old_communication_types;
RENAME TABLE new_communication_types TO communication_types;

RENAME TABLE communication_statuses TO old_communication_statuses;
RENAME TABLE new_communication_statuses TO communication_statuses;

DROP TABLE old_communication_types;
DROP TABLE old_communication_statuses;