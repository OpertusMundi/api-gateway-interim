--
-- Drop existing objects
--

DROP SCHEMA IF EXISTS "admin" CASCADE;

--
-- Admin schema
--

CREATE SCHEMA IF NOT EXISTS "admin"; 

--
-- Account
--

CREATE SEQUENCE "admin".account_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE "admin".account
(
  id                    integer                     NOT NULL  DEFAULT nextval('admin.account_id_seq'::regclass),
  "key"                 uuid                        NOT NULL,  
  "username"            character varying(120)      NOT NULL,
  "active"              boolean                     NOT NULL,
  "blocked"             boolean                     NOT NULL,
  "email"               character varying(120)      NOT NULL,
  "email_verified"      boolean                     NOT NULL,
  "email_verified_on"   timestamp                   NULL,
  "firstname"           character varying(64)       NULL,
  "lastname"            character varying(64)       NULL,
  "locale"              character varying(2)        NULL,
  "password"            character varying(64)       NULL,
  "created_on"          timestamp                   NOT NULL,
  "modified_on"         timestamp                   NOT NULL,
  "image_binary"        bytea,
  "image_mime_type"     character varying(30),
  "phone"               character varying(15),  
  "mobile"              character varying(15),
  CONSTRAINT pk_account PRIMARY KEY (id),
  CONSTRAINT uq_account_key UNIQUE ("key"),
  CONSTRAINT uq_account_email UNIQUE ("email"),
  CONSTRAINT uq_account_username UNIQUE ("username")
);

CREATE INDEX idx_admin_account_key ON "admin".account USING btree ("key");
CREATE INDEX idx_admin_account_username ON "admin".account USING btree ("username");

--
-- Role
--

CREATE SEQUENCE "admin".account_role_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE "admin".account_role
(
  id             integer                   NOT NULL  DEFAULT nextval('admin.account_role_id_seq'::regclass),
  "role"         character varying(64)     NOT NULL,
  "account"      integer                   NOT NULL,
  "granted_at"   timestamp                 NOT NULL  DEFAULT now(),
  "granted_by"   integer,
  CONSTRAINT pk_account_role PRIMARY KEY (id),
  CONSTRAINT fk_account_role_account FOREIGN KEY ("account")
      REFERENCES "admin".account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_account_role_granted_by FOREIGN KEY ("granted_by")
      REFERENCES "admin".account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uq_account_role UNIQUE ("account", "role")
); 
