DROP TABLE IF EXISTS web.address;
DROP TABLE IF EXISTS web.account_profile;

DROP SEQUENCE IF EXISTS web.address_id_seq;
DROP SEQUENCE IF EXISTS web.account_profile_id_seq;

--
-- Profile
--

CREATE SEQUENCE web.account_profile_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.account_profile
(
  id                            integer                     NOT NULL   DEFAULT nextval('web.account_profile_id_seq'::regclass),
  "account"                     integer                     NOT NULL,
  "email"                       character varying(120),
  "email_verified"              boolean                     NOT NULL,
  "email_verified_at"           timestamp                   NULL,  
  "mobile"                      character varying(15),
  "image_binary"                bytea,
  "image_mime_type"             character varying(30),
  "company"                     character varying(80),
  "company_type"                character varying(80),
  "vat"                         character varying(12),
  "country"                     character varying(40),
  "country_phone_code"          character varying(4),
  "phone"                       character varying(20),
  "additional_info"             text,
  "bank_account_iban"           character varying(40),
  "bank_account_swift"          character varying(40),
  "bank_account_holder_name"    character varying(40),
  "bank_account_currency"       character varying(4),
  "terms_accepted"              boolean                     NOT NULL  DEFAULT(false),
  "terms_accepted_at"           timestamp                   NULL,
  "site_url"                    character varying(80),
  "logo_image_binary"           bytea,
  "logo_image_mime_type"        character varying(30),
  "provider_verified_at"        timestamp                   NULL,
  "rating_count"                integer                     NOT NULL   DEFAULT(0),
  "rating_total"                integer                     NOT NULL   DEFAULT(0),
  "created_on"                  timestamp                   NOT NULL   DEFAULT now(),
  "modified_on"                 timestamp                   NOT NULL   DEFAULT now(),
  CONSTRAINT pk_account_profile PRIMARY KEY (id),
  CONSTRAINT uq_pk_account_profile_email UNIQUE ("email"),
  CONSTRAINT fk_account_profile_account FOREIGN KEY ("account")
      REFERENCES web.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

--
-- Address
--

CREATE SEQUENCE web.address_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.address
(
  id                    integer                     NOT NULL   DEFAULT nextval('web.address_id_seq'::regclass),
  "profile"             integer                     NOT NULL,
  "street"              character varying(120)      NOT NULL,
  "number"              character varying(10),
  "city"                character varying(120),
  "region"              character varying(80),
  "country"             character varying(40),
  "postal_code"         character varying(10),
  "floor_apartment"      character varying(15),
  "created_on"          timestamp                   NOT NULL   DEFAULT now(),
  "modified_on"         timestamp                   NOT NULL   DEFAULT now(),
  CONSTRAINT pk_address PRIMARY KEY (id),
  CONSTRAINT fk_adress_profile FOREIGN KEY ("profile")
      REFERENCES web.account_profile (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);