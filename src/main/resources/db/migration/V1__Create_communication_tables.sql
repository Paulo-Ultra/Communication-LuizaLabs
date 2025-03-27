CREATE TABLE communication_types (
                                     guid CHAR(36) PRIMARY KEY,
                                     code VARCHAR(50) NOT NULL UNIQUE,
                                     description VARCHAR(255) NOT NULL,
                                     active BOOLEAN DEFAULT TRUE,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IDX_GUID_COMMUNICATION_TYPES ON communication_types(guid);
CREATE INDEX IDX_TYPES_CODE_COMMUNICATION_TYPES ON communication_types(code);

CREATE TABLE communication_statuses (
                                        guid CHAR(36) PRIMARY KEY,
                                        code VARCHAR(50) NOT NULL UNIQUE,
                                        description VARCHAR(255) NOT NULL,
                                        is_final_state BOOLEAN DEFAULT FALSE,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IDX_GUID_COMMUNICATION_STATUSES ON communication_statuses(guid);
CREATE INDEX IDX_STATUSES_CODE_COMMUNICATION_STATUSES ON communication_statuses(code);

CREATE TABLE communication_schedules (
                                         guid CHAR(36) PRIMARY KEY,
                                         scheduled_date_time DATETIME NOT NULL,
                                         recipient VARCHAR(255) NOT NULL,
                                         message VARCHAR(1000) NOT NULL,
                                         type_guid CHAR(36) NOT NULL,
                                         status_guid CHAR(36) NOT NULL,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         FOREIGN KEY (type_guid) REFERENCES communication_types(guid),
                                         FOREIGN KEY (status_guid) REFERENCES communication_statuses(guid)
);

CREATE INDEX IDX_GUID_COMMUNICATION_SCHEDULES ON communication_schedules(guid);
CREATE INDEX IDX_SCHEDULED_DATETIME ON communication_schedules(scheduled_date_time);
CREATE INDEX IDX_STATUS ON communication_schedules(status_guid);