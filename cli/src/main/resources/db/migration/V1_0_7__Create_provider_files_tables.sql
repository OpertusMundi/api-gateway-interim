--
-- Drop existing objects
--

DROP SEQUENCE IF EXISTS web.provider_registration_file_id_seq CASCADE;
DROP TABLE IF EXISTS web.provider_registration_file CASCADE;

DROP SEQUENCE IF EXISTS web.account_profile_provider_file_id_seq CASCADE;
DROP TABLE IF EXISTS web.account_profile_provider_file CASCADE;

--
-- Provider registartion files
-- 

CREATE SEQUENCE web.provider_registration_file_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.provider_registration_file
(
  id                         integer                     NOT NULL   DEFAULT nextval('web.provider_registration_file_id_seq'::regclass),
  "registration"             integer                     NOT NULL,
  "file"                     integer                     NOT NULL,
  "status"                   character varying(12)       NOT NULL,
  "review"                   character varying,
  "created_on"               timestamp                   NOT NULL,
  "modified_on"              timestamp                	 NOT NULL,
  "modified_by"              integer,
  CONSTRAINT pk_provider_registration_file PRIMARY KEY (id),
  CONSTRAINT fk_provider_registration_file_registration FOREIGN KEY ("registration")
      REFERENCES web.provider_registration (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_provider_registration_file_file FOREIGN KEY ("file")
      REFERENCES "file".file_upload (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_provider_registration_file_modified_by FOREIGN KEY ("modified_by")
      REFERENCES "web".account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

--
-- Profile KYC files
-- 

CREATE SEQUENCE web.account_profile_provider_file_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.account_profile_provider_file
(
  id                         integer                     NOT NULL   DEFAULT nextval('web.account_profile_provider_file_id_seq'::regclass),
  "profile"                  integer                     NOT NULL,
  "file"                     integer                     NOT NULL,
  "added_on"                 timestamp                	 NOT NULL,
  "added_by"                 integer,
  CONSTRAINT pk_account_profile_provider_file PRIMARY KEY (id),
  CONSTRAINT fk_account_profile_provider_file_profile FOREIGN KEY ("profile")
      REFERENCES "web".account_profile (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_account_profile_provider_file_file FOREIGN KEY ("file")
      REFERENCES "file".file_upload (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_account_profile_provider_file_added_by FOREIGN KEY ("added_by")
      REFERENCES "web".account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);
