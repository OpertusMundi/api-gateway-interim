--
-- Extensions
--
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--
-- Drop existing objects
--

DROP SCHEMA IF EXISTS "web" CASCADE;
DROP SCHEMA IF EXISTS "logging" CASCADE;

--
-- Web schema
--

CREATE SCHEMA IF NOT EXISTS "web"; 

--
-- Session
--

CREATE TABLE web.spring_session (
	PRIMARY_ID CHAR(36) NOT NULL,
	SESSION_ID CHAR(36) NOT NULL,
	CREATION_TIME BIGINT NOT NULL,
	LAST_ACCESS_TIME BIGINT NOT NULL,
	MAX_INACTIVE_INTERVAL INT NOT NULL,
	EXPIRY_TIME BIGINT NOT NULL,
	PRINCIPAL_NAME VARCHAR(100),
	CONSTRAINT spring_session_pk PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX spring_session_ix1 ON web.spring_session (SESSION_ID);
CREATE INDEX spring_session_ix2 ON web.spring_session (EXPIRY_TIME);
CREATE INDEX spring_session_ix3 ON web.spring_session (PRINCIPAL_NAME);

CREATE TABLE web.spring_session_attributes (
	SESSION_PRIMARY_ID CHAR(36) NOT NULL,
	ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
	ATTRIBUTE_BYTES BYTEA NOT NULL,
	CONSTRAINT spring_session_attributes_pk PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
	CONSTRAINT spring_session_attributes_fl FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES web.spring_session(PRIMARY_ID) ON DELETE CASCADE
);

--
-- Logging
--

CREATE SCHEMA IF NOT EXISTS "logging"; 

CREATE SEQUENCE logging.log4j_message_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 128;

CREATE TABLE logging.log4j_message
(
  "id"             bigint PRIMARY KEY            NOT NULL  DEFAULT nextval('logging.log4j_message_id_seq'::regclass),
  "application"    character varying(64)         NOT NULL,
  "generated"      timestamp,
  "level"          character varying(12),
  "message"        text,
  "throwable"      text,
  "logger"         character varying(256),
  "client_address" character varying(16),
  "username"       character varying(64)
);
