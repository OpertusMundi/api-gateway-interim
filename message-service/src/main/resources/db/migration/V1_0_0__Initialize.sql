-- Add required extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Add default schema
CREATE SCHEMA IF NOT EXISTS "messaging"; 

--
-- Message
--

CREATE SEQUENCE messaging.message_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE messaging.message
(
  id                    integer                     NOT NULL  DEFAULT nextval('messaging.message_id_seq'::regclass),
  "key"                 uuid                        NOT NULL,  
  "thread"              uuid                        NOT NULL,
  "owner"               uuid                        NOT NULL,
  "sender"              uuid                        NOT NULL,
  "recipient"           uuid                        NOT NULL,
  "text"                character varying           NOT NULL,
  "send_at"             timestamp                   NOT NULL,
  "read"                boolean                     NOT NULL,
  "read_at"             timestamp,
  CONSTRAINT pk_message     PRIMARY KEY (id),
  CONSTRAINT uq_message_owner_key UNIQUE ("owner", "key")
);

CREATE INDEX IF NOT EXISTS idx_message_owner ON messaging.message USING btree (owner);

--
-- Notification
--

CREATE SEQUENCE messaging.notification_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE messaging.notification
(
  id                    integer                     NOT NULL  DEFAULT nextval('messaging.notification_id_seq'::regclass),
  "key"                 uuid                        NOT NULL,  
  "recipient"           uuid                        NOT NULL,
  "text"                character varying           NOT NULL,
  "send_at"             timestamp                   NOT NULL,
  "read"                boolean                     NOT NULL,
  "read_at"             timestamp,
  CONSTRAINT pk_notification     PRIMARY KEY (id),
  CONSTRAINT uq_notification_key UNIQUE ("key")
);

CREATE INDEX IF NOT EXISTS idx_notification_recipient ON messaging.notification USING btree (recipient);
