CREATE TABLE web_service_provider (
    id              BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(128),
    entity_id       VARCHAR(128),
    subject         VARCHAR(256) NOT NULL,
    certificate     BLOB(4096) NOT NULL,

    UNIQUE (entity_id)
);

CREATE TABLE web_service_consumer (
    id              BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(128) NOT NULL,
    subject         VARCHAR(256) NOT NULL,
    certificate     BLOB(4096) NOT NULL,

    UNIQUE(subject)
);
	
CREATE TABLE granted_access (
    wsc_id          BIGINT NOT NULL,
    wsp_id          BIGINT NOT NULL,

    FOREIGN KEY (wsc_id) REFERENCES web_service_consumer(id) ON DELETE CASCADE,
    FOREIGN KEY (wsp_id) REFERENCES web_service_provider(id) ON DELETE CASCADE
);
