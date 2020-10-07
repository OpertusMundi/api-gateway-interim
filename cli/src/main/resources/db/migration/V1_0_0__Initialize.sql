-- Add required extensions
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Add default schema
CREATE SCHEMA IF NOT EXISTS "web"; 

--
-- Account
--

CREATE SEQUENCE web.account_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.account
(
  id                    integer                     NOT NULL  DEFAULT nextval('web.account_id_seq'::regclass),
  "key"                 uuid                        NOT NULL,  
  "username"            character varying(120)      NOT NULL,
  "active"              boolean                     NOT NULL,
  "blocked"             boolean                     NOT NULL,
  "email"               character varying(120)      NOT NULL,
  "email_verified"      boolean                     NOT NULL,
  "email_verified_at"   timestamp                   NULL,
  "firstname"           character varying(64)       NULL,
  "lastname"            character varying(64)       NULL,
  "locale"              character varying(2)        NULL,
  "password"            character varying(64)       NULL,
  "registered_at"       timestamp                   NOT NULL  DEFAULT now(),
  "activation_status"   character varying(20)       NOT NULL  DEFAULT ('PENDING'),
  "activation_at"       timestamp                   NULL,
  "idp_name"            character varying(20)       NULL,
  "idp_user_alias"      character varying(120)      NULL,
  "idp_user_image"      text                        NULL,
  CONSTRAINT pk_account PRIMARY KEY (id),
  CONSTRAINT uq_account_email UNIQUE ("email"),
  CONSTRAINT uq_account_username UNIQUE ("username"),
  CONSTRAINT uq_account_key UNIQUE ("key")
);

CREATE INDEX idx_account_username ON web.account USING btree (username COLLATE pg_catalog."default");

--
-- Activation
--

CREATE SEQUENCE web.activation_token_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.activation_token 
(
  "id"                  integer                     NOT NULL  DEFAULT nextval('web.activation_token_id_seq'::regclass),
  "account"             integer                     NOT NULL,
  "email"               character varying(120)      NOT NULL,
  "type"                character varying(20)       NOT NULL,
  "token"               uuid                        NOT NULL,
  "created_at"          timestamp                   NOT NULL,
  "redeemed_at"         timestamp                   NULL,
  "valid"				boolean						NOT NULL  DEFAULT(true),
  "duration"            integer                     NOT NULL  DEFAULT(1),
  CONSTRAINT pk_mail_verification PRIMARY KEY (id),
  CONSTRAINT fk_mail_verification_account FOREIGN KEY ("account")
      REFERENCES web.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);
--
-- Role
--

CREATE SEQUENCE web.account_role_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.account_role
(
  id             integer                   NOT NULL  DEFAULT nextval('web.account_role_id_seq'::regclass),
  "role"         character varying(64)     NOT NULL,
  "account"      integer                   NOT NULL,
  "granted_at"   timestamp                 NOT NULL  DEFAULT now(),
  "granted_by"   integer,
  CONSTRAINT pk_account_role PRIMARY KEY (id),
  CONSTRAINT fk_account_role_account FOREIGN KEY ("account")
      REFERENCES web.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_account_role_granted_by FOREIGN KEY ("granted_by")
      REFERENCES web.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uq_account_role UNIQUE ("account", "role")
); 

--
-- Event
--

CREATE SEQUENCE web.log4j_message_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 128;

CREATE TABLE web.log4j_message
(
  "id"             bigint PRIMARY KEY            NOT NULL  DEFAULT nextval('web.log4j_message_id_seq'::regclass),
  "application"    character varying(64)         NOT NULL,
  "generated"      timestamp,
  "level"          character varying(12),
  "message"        text,
  "throwable"      text,
  "logger"         character varying(256),
  "client_address" character varying(16),
  "username"       character varying(64)
);

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

