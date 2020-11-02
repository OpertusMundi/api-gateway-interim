--
-- Drop existing objects
--

DROP SEQUENCE IF EXISTS web.provider_registration_id_seq CASCADE;
DROP TABLE IF EXISTS web.provider_registration CASCADE;

DROP SEQUENCE IF EXISTS web.account_profile_id_seq CASCADE;
DROP TABLE IF EXISTS web.account_profile CASCADE;

DROP SEQUENCE IF EXISTS web.account_profile_hist_id_seq CASCADE;
DROP TABLE IF EXISTS web.account_profile_hist CASCADE;

DROP SEQUENCE IF EXISTS web.address_id_seq CASCADE;
DROP TABLE IF EXISTS web.address CASCADE;

--
-- Provider registartion
-- 

CREATE SEQUENCE web.provider_registration_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.provider_registration
(
  id                         integer                     NOT NULL   DEFAULT nextval('web.provider_registration_id_seq'::regclass),
  "account"                  integer                     NOT NULL,
  --
  -- Provider fields
  --
  "additional_info"          text,
  "bank_account_currency"    character varying(4),
  "bank_account_holder_name" character varying(40),
  "bank_account_iban"        character varying(40),
  "bank_account_swift"       character varying(40),
  "company"                  character varying(80),
  "company_type"             character varying(80),
  "contract"                 uuid                        NULL,
  "country"                  character varying(40),
  "country_phone_code"       character varying(4),
  "email"                    character varying(120), 
  "logo_image_binary"        bytea,
  "logo_image_mime_type"     character varying(30),
  "phone"                    character varying(20),
  "site_url"                 character varying(80),
  "vat"                      character varying(12)       NOT NULL,
  --
  -- Other fields
  --
  "created_on"               timestamp                	 NOT NULL,
  "modified_on"              timestamp                	 NOT NULL,
  "status"                   character varying(12)    	 NOT NULL,
  CONSTRAINT pk_account_provider_registration PRIMARY KEY (id),
  CONSTRAINT fk_account_provider_registration_account FOREIGN KEY ("account")
      REFERENCES web.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

--
-- Profile
--

CREATE SEQUENCE web.account_profile_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.account_profile
(
  id                                  integer                     NOT NULL   DEFAULT nextval('web.account_profile_id_seq'::regclass),
  "account"                           integer                     NOT NULL,
  --
  -- Provider fields
  --
  "provider_additional_info"          text,
  "provider_bank_account_currency"    character varying(4),
  "provider_bank_account_holder_name" character varying(40),
  "provider_bank_account_iban"        character varying(40),
  "provider_bank_account_swift"       character varying(40),
  "provider_company"                  character varying(80),
  "provider_company_type"             character varying(80),
  "provider_contract"                 uuid,
  "provider_country"                  character varying(40),
  "provider_country_phone_code"       character varying(4),
  -- Provider public email. Displayed only if verified.
  "provider_email"                    character varying(120),
  "provider_email_verified"           boolean,
  "provider_email_verified_at"        timestamp,  
  "provider_logo_image_binary"        bytea,
  "provider_logo_image_mime_type"     character varying(30),
  "provider_phone"                    character varying(20),
  -- Provider rating attributes, updated periodically by the analytics service
  "provider_rating_count"             integer                     NOT NULL,
  "provider_rating_total"             integer                     NOT NULL,  
  -- Provider current registration record. A registration record is created the first
  -- time a user becomes a provider and whenever an existing provider is updated.
  "provider_registration"             integer,
  "provider_site_url"                 character varying(80),
  "provider_terms_accepted"           boolean                     NOT NULL,
  "provider_terms_accepted_at"        timestamp,
  "provider_vat"                      character varying(12)       NULL,
  -- Initial registration
  "provider_registered_on"            timestamp without time zone NULL,
  "provider_modified_on"              timestamp without time zone NULL,
  --
  -- Consumer fields
  -- 
  "consumer_billing_address"          integer,
  "consumer_shipping_address"         integer,
  "consumer_vat"                      character varying(12),
  "consumer_registered_on"            timestamp without time zone,
  "consumer_modified_on"              timestamp without time zone,
  --
  -- Profile fields
  ---
  "created_on"                  timestamp                   NOT NULL   DEFAULT now(),
  "modified_on"                 timestamp                   NOT NULL   DEFAULT now(),
  "image_binary"                bytea,
  "image_mime_type"             character varying(30),
  "phone"                       character varying(15),  
  "mobile"                      character varying(15),
  CONSTRAINT pk_account_profile PRIMARY KEY (id),
  CONSTRAINT uq_pk_account_profile_provider_email UNIQUE ("provider_email"),
  CONSTRAINT fk_account_profile_account FOREIGN KEY ("account")
      REFERENCES web.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_account_profile_provider_registration FOREIGN KEY ("provider_registration")
      REFERENCES web.provider_registration (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION   
);

--
-- Profile history
--

CREATE SEQUENCE web.account_profile_hist_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.account_profile_hist
(
  id                                  integer                     NOT NULL   DEFAULT nextval('web.account_profile_hist_id_seq'::regclass),
  "profile"                           integer                     NOT NULL,
  "provider_additional_info"          text,
  --
  -- Provider fields
  --
  "provider_bank_account_currency"    character varying(4),
  "provider_bank_account_holder_name" character varying(40),
  "provider_bank_account_iban"        character varying(40),
  "provider_bank_account_swift"       character varying(40),
  "provider_company"                  character varying(80),
  "provider_company_type"             character varying(80),
  "provider_contract"                 uuid,
  "provider_country"                  character varying(40),
  "provider_country_phone_code"       character varying(4),
  "provider_email"                    character varying(120),
  "provider_email_verified"           boolean,
  "provider_email_verified_at"        timestamp,  
  "provider_logo_image_binary"        bytea,
  "provider_logo_image_mime_type"     character varying(30),
  "provider_phone"                    character varying(20),
  "provider_rating_count"             integer,
  "provider_rating_total"             integer,
  "provider_registration"             integer,
  "provider_site_url"                 character varying(80),
  "provider_terms_accepted"           boolean,
  "provider_terms_accepted_at"        timestamp,
  "provider_vat"                      character varying(12),
  "provider_registered_on"            timestamp without time zone,
  "provider_modified_on"              timestamp without time zone,
  --
  -- Consumer fields
  -- 
  "consumer_billing_address"          integer,
  "consumer_shipping_address"         integer,
  "consumer_vat"                      character varying(12),
  "consumer_registered_on"            timestamp without time zone,
  "consumer_modified_on"              timestamp without time zone,
  --
  -- Account & Profile fields
  ---
  "created_on"                  timestamp,
  "modified_on"                 timestamp,
  "image_binary"                bytea,
  "image_mime_type"             character varying(30),
  "phone"                       character varying(15),  
  "mobile"                      character varying(15),
  "firstname"                   character varying(64),
  "lastname"                    character varying(64),
  "locale"                      character varying(2),
  CONSTRAINT pk_account_profile_hist PRIMARY KEY (id),
  CONSTRAINT fk_account_profile_hist_profile FOREIGN KEY ("profile")
      REFERENCES web.account_profile (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

--
-- Address
--

CREATE SEQUENCE web.address_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.address
(
  id                    integer                     NOT NULL   DEFAULT nextval('web.address_id_seq'::regclass),
  "key"                 uuid                        NOT NULL,
  "profile"             integer                     NOT NULL,
  "street"              character varying(120)      NOT NULL,
  "number"              character varying(10),
  "city"                character varying(120),
  "region"              character varying(80),
  "country"             character varying(40),
  "postal_code"         character varying(10),
  "floor_apartment"     character varying(15),
  "created_on"          timestamp                   NOT NULL   DEFAULT now(),
  "modified_on"         timestamp                   NOT NULL   DEFAULT now(),
  "type"                character varying(12)       NOT NULL,
  "default"				boolean						NOT NULL,
  CONSTRAINT pk_address PRIMARY KEY (id),
  CONSTRAINT uq_pk_address_key UNIQUE ("key"),
  CONSTRAINT fk_adress_profile FOREIGN KEY ("profile")
      REFERENCES web.account_profile (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

ALTER TABLE web.account_profile ADD CONSTRAINT fk_account_profile_consumer_billing_address FOREIGN KEY ("consumer_billing_address")
      REFERENCES web.address (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;
      
ALTER TABLE web.account_profile ADD CONSTRAINT fk_account_profile_consumer_shipping_address FOREIGN KEY ("consumer_shipping_address")
      REFERENCES web.address (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;
