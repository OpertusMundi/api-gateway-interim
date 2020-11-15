--
-- Drop existing objects
--

DROP SEQUENCE IF EXISTS web.customer_draft_id_seq CASCADE;
DROP TABLE IF EXISTS web.customer_draft CASCADE;
DROP TABLE IF EXISTS web.customer_draft_individual CASCADE;
DROP TABLE IF EXISTS web.customer_draft_professional CASCADE;

DROP SEQUENCE IF EXISTS web.customer_id_seq CASCADE;
DROP TABLE IF EXISTS web.customer CASCADE;
DROP TABLE IF EXISTS web.customer_individual CASCADE;
DROP TABLE IF EXISTS web.customer_professional CASCADE;

DROP SEQUENCE IF EXISTS web.account_profile_id_seq CASCADE;
DROP TABLE IF EXISTS web.account_profile CASCADE;

--
-- Customer draft tables
--

CREATE SEQUENCE web.customer_draft_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.customer_draft (
    "id"                       integer                        NOT NULL DEFAULT nextval('web.customer_draft_id_seq'::regclass),
    "key"                      uuid                           NOT NULL,
    "idk_user"                 uuid                           NOT NULL,
    "idk_wallet"               uuid                           NOT NULL,
    "idk_account"              uuid                           NOT NULL,
    "account"                  integer                        NOT NULL,
    -- Person Type: Can be INDIVIDUAL(1) or PROFESSIONAL(2)
    "type"                     integer                        NOT NULL,
    -- Unique user Id returned by the payment provider API
    "payment_provider_user"    character varying,
    -- Unique wallet Id returned by the payment provider API    
    "payment_provider_wallet"  character varying,
    "email"                    character varying,
    "status"                   character varying(12)    	  NOT NULL,
    "created_at"               timestamp without time zone    NOT NULL,
    "modified_at"              timestamp without time zone    NOT NULL,
    CONSTRAINT pk_customer_draft PRIMARY KEY ("id"),
    CONSTRAINT fk_customer_draft_account FOREIGN KEY ("account")
        REFERENCES web.account ("id") MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE web.customer_draft_individual (
    "id"                        integer                        PRIMARY KEY,
    ---
    --- MANGOPAY fields
    ---
    "firstname"                 character varying,
    "lastname"                  character varying,
    "address_line1"             character varying,
    "address_line2"             character varying,
    "address_city"              character varying,
    "address_region"            character varying,
    "address_postal_code"       character varying,
    "address_country"           character varying,
    "birthdate"                 timestamp without time zone,
    "nationality"               character varying,
    "country_of_residence"      character varying,
    "occupation"                character varying,
    CONSTRAINT fk_customer_draft_individual_customer_draft
        FOREIGN KEY ("id") REFERENCES web.customer_draft ("id")
        ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE web.customer_draft_professional (
    "id"                                          integer                        PRIMARY KEY,
    ---
    --- MANGOPAY fields
    ---
    "headquarters_address_line1"                  character varying,
    "headquarters_address_line2"                  character varying,
    "headquarters_address_city"                   character varying,
    "headquarters_address_region"                 character varying,
    "headquarters_address_postal_code"            character varying,
    "headquarters_address_country"                character varying,
    "legal_person_type"                           character varying,
    "name"                                        character varying,
    "legal_representative_address_line1"          character varying,
    "legal_representative_address_line2"          character varying,
    "legal_representative_address_city"           character varying,
    "legal_representative_address_region"         character varying,
    "legal_representative_address_postal_code"    character varying,
    "legal_representative_address_country"        character varying,
    "legal_representative_birthdate"              timestamp without time zone,
    "legal_representative_country_of_residence"   character varying,
    "legal_representative_nationality"            character varying,
    "legal_representative_email"                  character varying,
    "legal_representative_first_name"             character varying,
    "legal_representative_last_name"              character varying,
    "company_number"                              character varying,   
    "bank_account_owner_name"                     character varying,
    "bank_account_owner_address_line1"            character varying,
    "bank_account_owner_address_line2"            character varying,
    "bank_account_owner_address_city"             character varying,
    "bank_account_owner_address_region"           character varying,
    "bank_account_owner_address_postal_code"      character varying,
    "bank_account_owner_address_country"          character varying,
    "bank_account_iban"                           character varying,
    "bank_account_bic"                            character varying,
    "bank_account_tag"                            character varying,
    ---
    --- OpertusMundi fields
    ---
    "additional_info"                             text,
    "company_type"                                character varying(80),
    "logo_image_binary"                           bytea,
    "logo_image_mime_type"                        character varying(30),
    -- Unique bank account Id returned by the payment provider API
    "payment_provider_bank_account"               character varying,
    "phone"                                       character varying(20),
    "site_url"                                    character varying(80),
    CONSTRAINT fk_customer_draft_professional_customer_draft
        FOREIGN KEY ("id") REFERENCES web.customer_draft ("id")
        ON UPDATE NO ACTION ON DELETE CASCADE
);

--
-- provider/consumer
--

CREATE SEQUENCE web.customer_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.customer (
    "id"                        integer                        NOT NULL DEFAULT nextval('web.customer_id_seq'::regclass),
    "draft_key"                 uuid                           NOT NULL,
    "account"                   integer                        NOT NULL,
    -- Person Type: Can be INDIVIDUAL or PROFESSIONAL    
    "type"                      integer                        NOT NULL,
    -- Unique user Id returned by the payment provider API
    "payment_provider_user"     character varying              NOT NULL,
    -- Unique wallet Id returned by the payment provider API    
    "payment_provider_wallet"   character varying              NOT NULL,
    -- KYC Level: Can be LIGHT or REGULAR
    "kyc_level"                 character varying              NOT NULL,
    -- Contract unique ref code
    "contract"                  uuid,
    "email"                     character varying              NOT NULL,
    "email_verified"            boolean                        NOT NULL,
    "email_verified_at"         timestamp without time zone,
    "terms_accepted"            boolean                        NOT NULL,
    "terms_accepted_at"         timestamp,
    "created_at"                timestamp without time zone    NOT NULL,
    "modified_at"               timestamp without time zone    NOT NULL,
    CONSTRAINT pk_customer PRIMARY KEY ("id"),
    CONSTRAINT fk_customer_draft_account FOREIGN KEY ("account")
        REFERENCES web.account ("id") MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE web.customer_individual (
    "id"                        integer                        PRIMARY KEY,
    ---
    --- MANGOPAY fields
    ---
    "firstname"                 character varying              NOT NULL,
    "lastname"                  character varying              NOT NULL,
    "address_line1"             character varying              NOT NULL,
    "address_line2"             character varying,
    "address_city"              character varying              NOT NULL,
    "address_region"            character varying              NOT NULL,
    "address_postal_code"       character varying              NOT NULL,
    "address_country"           character varying              NOT NULL,
    "birthdate"                 timestamp without time zone    NOT NULL,
    "nationality"               character varying              NOT NULL,
    "country_of_residence"      character varying              NOT NULL,
    "occupation"                character varying,
    CONSTRAINT fk_customer_individual_customer
        FOREIGN KEY ("id") REFERENCES web.customer ("id")
        ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE web.customer_professional (
    "id"                                        integer                        PRIMARY KEY,
    ---
    --- MANGOPAY fields
    ---
    "headquarters_address_line1"                character varying              NOT NULL,
    "headquarters_address_line2"                character varying,
    "headquarters_address_city"                 character varying              NOT NULL,
    "headquarters_address_region"               character varying              NOT NULL,
    "headquarters_address_postal_code"          character varying              NOT NULL,
    "headquarters_address_country"              character varying              NOT NULL,
    "legal_person_type"                         character varying              NOT NULL,
    "name"                                      character varying              NOT NULL,
    "legal_representative_address_line1"        character varying,
    "legal_representative_address_line2"        character varying,
    "legal_representative_address_city"         character varying,
    "legal_representative_address_region"       character varying,
    "legal_representative_address_postal_code"  character varying,
    "legal_representative_address_country"      character varying,
    "legal_representative_birthdate"            timestamp without time zone    NOT NULL,
    "legal_representative_country_of_residence" character varying              NOT NULL,
    "legal_representative_nationality"          character varying              NOT NULL,
    "legal_representative_email"                character varying              NOT NULL,
    "legal_representative_first_name"           character varying              NOT NULL,
    "legal_representative_last_name"            character varying              NOT NULL,
    "company_number"                            character varying              NOT NULL,
    "bank_account_owner_name"                   character varying              NOT NULL,
    "bank_account_owner_address_line1"          character varying              NOT NULL,
    "bank_account_owner_address_line2"          character varying,
    "bank_account_owner_address_city"           character varying              NOT NULL,
    "bank_account_owner_address_region"         character varying              NOT NULL,
    "bank_account_owner_address_postal_code"    character varying              NOT NULL,
    "bank_account_owner_address_country"        character varying              NOT NULL,
    "bank_account_iban"                         character varying              NOT NULL,
    "bank_account_bic"                          character varying              NOT NULL,
    "bank_account_tag"                          character varying,
    ---
    --- OpertusMundi fields
    ---
    "additional_info"                           text,
    "company_type"                              character varying(80),
    "logo_image_binary"                         bytea,
    "logo_image_mime_type"                      character varying(30),
    -- Unique bank account Id returned by the payment provider API
    "payment_provider_bank_account"             character varying,
    "phone"                                     character varying(20),
    "rating_count"                              integer                        NOT NULL,
    "rating_total"                              integer                        NOT NULL,  
    "site_url"                                  character varying(80),
    CONSTRAINT fk_customer_professional_customer
        FOREIGN KEY ("id") REFERENCES web.customer ("id")
        ON UPDATE NO ACTION ON DELETE CASCADE
);

--
-- Profile
--

CREATE SEQUENCE web.account_profile_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE web.account_profile
(
  "id"                                integer                     NOT NULL   DEFAULT nextval('web.account_profile_id_seq'::regclass),
  "account"                           integer                     NOT NULL,
  "consumer"                          integer,
  "provider"                          integer,
  -- consumer current registration record. A registration record is created the first
  -- time a user becomes a consumer and whenever an existing consumer is updated.
  "consumer_registration"             integer,
  -- provider current registration record. A registration record is created the first
  -- time a user becomes a provider and whenever an existing provider is updated.
  "provider_registration"             integer,
  "image_binary"                      bytea,
  "image_mime_type"                   character varying(30),
  "phone"                             character varying(15),  
  "mobile"                            character varying(15),
  "created_at"                        timestamp                   NOT NULL,
  "modified_at"                       timestamp                   NOT NULL,
  CONSTRAINT pk_account_profile PRIMARY KEY ("id"),
  CONSTRAINT fk_account_profile_account FOREIGN KEY ("account")
      REFERENCES web.account ("id") MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_account_profile_consumer FOREIGN KEY ("consumer")
      REFERENCES web.customer ("id") MATCH SIMPLE,
  CONSTRAINT fk_account_profile_provider FOREIGN KEY ("provider")
      REFERENCES web.customer_professional ("id") MATCH SIMPLE,
  CONSTRAINT fk_account_profile_consumer_registration FOREIGN KEY ("consumer_registration")
      REFERENCES web.customer_draft ("id") MATCH SIMPLE,
  CONSTRAINT fk_account_profile_provider_registartion FOREIGN KEY ("provider_registration")
      REFERENCES web.customer_draft_professional ("id") MATCH SIMPLE
);
