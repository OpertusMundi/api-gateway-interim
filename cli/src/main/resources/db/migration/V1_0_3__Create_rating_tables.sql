CREATE SCHEMA IF NOT EXISTS "rating"; 

DROP TABLE IF EXISTS rating.asset;
DROP TABLE IF EXISTS rating.provider;

DROP SEQUENCE IF EXISTS rating.rating_id_seq;

CREATE SEQUENCE rating.rating_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE rating.asset
(
  "id"                              bigint                  NOT NULL   DEFAULT nextval('rating.rating_id_seq'::regclass),
  "asset"                           uuid                    NOT NULL,  
  "account"                         uuid                    NOT NULL,
  "value"                           numeric(2,1)            NOT NULL,
  "comment"                         character varying       NOT NULL,
  "created_on"                      timestamp               NOT NULL   DEFAULT now(),
  CONSTRAINT pk_asset PRIMARY KEY (id)
);

CREATE TABLE rating.provider
(
  "id"                              bigint                  NOT NULL   DEFAULT nextval('rating.rating_id_seq'::regclass),
  "provider"                        uuid                    NOT NULL,  
  "account"                         uuid                    NOT NULL,
  "value"                           numeric(2,1)            NOT NULL,
  "comment"                         character varying       NOT NULL,
  "created_on"                      timestamp               NOT NULL   DEFAULT now(),
  CONSTRAINT pk_provider PRIMARY KEY (id)
);
