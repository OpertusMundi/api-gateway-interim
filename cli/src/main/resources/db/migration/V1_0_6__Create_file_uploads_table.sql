--
-- Drop existing objects
--

DROP SCHEMA IF EXISTS "file" CASCADE;

--
-- File manager schema
--

CREATE SCHEMA IF NOT EXISTS "file"; 

--
-- File uploads
--

CREATE SEQUENCE "file".file_upload_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE "file".file_upload
(
  "id"                              integer                 NOT NULL   DEFAULT nextval('file.file_upload_id_seq'::regclass),
  "key"                             uuid                    NOT NULL,
  "owning_entity_key"               uuid                    NOT NULL,
  "owning_entity_type"              character varying(60)   NOT NULL,
  "created_on"                      timestamp               NOT NULL,
  "uploaded_by"                     integer                 NULL,
  "file_name"                       character varying       NOT NULL,
  "relative_path"                   character varying       NOT NULL,
  "size"                            bigint                  NOT NULL,
  "comment"                        character varying,
  CONSTRAINT pk_file_upload PRIMARY KEY (id),
  CONSTRAINT uq_file_upload_key UNIQUE ("key"),
  CONSTRAINT fk_file_upload_uploaded_by FOREIGN KEY ("uploaded_by")
      REFERENCES web.account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
);

CREATE INDEX idx_file_upload_key ON "file".file_upload USING btree ("key");
CREATE INDEX idx_file_upload_owning_entity_key ON "file".file_upload USING btree ("owning_entity_key");

